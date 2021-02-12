package ru.otus.hw04.gc;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectArrayMessage;

/**
 * GcLogger monitors GC activity and produces log for further analysis.
 *
 */
public class GcLogger
{

    // output log
    private final Logger LOG = LogManager.getLogger("gclogger.csv");

    // For statistics in last n ms window
    //
    // edgePools table accumulates several statistics since the last purge.
    // structure: gcOperation -> measureName -> value
    private volatile Map< String, Map<String, Double> > edgePool;
    // edgeDuration determines how often we should log edge statistics and purge edgePool.
    // in ms
    private final double edgeDuration;
    // edgeCount counts the number of edgePool purges
    private volatile long edgeCount;


    /**
     * Creates a new GcLogger object.
     *
     * @param edgeDuration period of reporting, in ms
     */
    public GcLogger( final double edgeDuration )
    {
        this.edgePool = new HashMap<>();
        this.edgeDuration = edgeDuration;
        this.edgeCount = 0;
    }

    /**
     * Turns on GC monitoring and statistics logging.
     *
     */
    public void startMonitoring() {
        final List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (final GarbageCollectorMXBean gcbean : gcbeans) {
            final NotificationEmitter emitter = (NotificationEmitter) gcbean;
            final NotificationListener listener = (notification, handback) -> {
                if ( notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION) ) {
                    final GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    final String gcName = info.getGcName();
                    final String gcAction = info.getGcAction();
                    final String gcCause = info.getGcCause();
                    final String gcOperation = gcName+" | "+gcAction+" | "+gcCause;

                    final long startTime = info.getGcInfo().getStartTime();
                    final long duration = info.getGcInfo().getDuration();

                    // populate map with basic measures
                    final Map<String, Double> measures = new HashMap<>();
                    measures.put( "Count",   1d );
                    measures.put( "Start time", Double.valueOf( startTime ) );
                    measures.put( "Duration",   Double.valueOf( duration  ) );

                    // accumulate edgePool
                    if ( !edgePool.containsKey( gcOperation ) ) {
                        edgePool.put( gcOperation, measures );
                    }
                    else {
                        final Map<String, Double> edgeMeasures = edgePool.get( gcOperation );
                        for ( final Map.Entry<String, Double> e : measures.entrySet() )  {
                            final String key = e.getKey();
                            Double newValue = e.getValue();
                            if ( edgeMeasures.containsKey( key ) ) {
                                newValue += edgeMeasures.get( key );
                            }
                            edgeMeasures.put( key, newValue );
                        }
                    }

                    // if edgePool is filled up
                    if ( startTime - edgeDuration * edgeCount > edgeDuration ) {
                        edgeCount = (long)( startTime/edgeDuration );

                        // log statistics
                        for ( final String group : edgePool.keySet() ) {
                            final double count = edgePool.get(group).get("Count");
                            for ( final Map.Entry<String, Double> e : edgePool.get(group).entrySet() )  {
                                LOG.info( new ObjectArrayMessage( startTime, group, e.getKey() + " | SUM", e.getValue() ) );
                                LOG.info( new ObjectArrayMessage( startTime, group, e.getKey() + " | AVG", e.getValue() / count ) );
                            }
                        }

                        // purge edgePool
                        edgePool.clear();
                    }
                }
            };

            emitter.addNotificationListener(listener, null, null);

        }
    }
}


