package ru.otus.hw04.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectArrayMessage;

/**
 * Utility class to test JVM
 *
 */
public class JvmTests
{

    /**
     * Start process with increasing memory load
     *  
     * @param crashAfter milliseconds till expected OutOfMemory
     */
    public static Thread startIncreasingMemoryLoad(final long crashAfter)
            throws Exception
        {
            Runnable loadTask = () -> {
                // maxmimum heap memory -Xmx
                final long maxMemory = Runtime.getRuntime().maxMemory();
                // numberOfIterations till expected OOM
                final long numberOfIterations = 1000;
                final long step = ( maxMemory / numberOfIterations / 16 );
                // pause between itertaions
                final long delayValue = crashAfter / numberOfIterations;
                // dummy list of records
                final List<String> report = new ArrayList<>();
                int arraySize = 1;
                while ( !Thread.interrupted() ) {
                    for ( int i = 0; i < arraySize; i++ ) {
                        final String record = new String("record0"); // occupies about 32 bytes
                        if ( report.size() <= i ) {
                            report.add( i, record );
                        }
                        else {
                            report.set( i, record );
                        }
                    }
                    arraySize += step;
                    try {
                        Thread.sleep( delayValue );
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    }
                return;
                };
                Thread thread = new Thread(loadTask);
                thread.start();
                return thread;
            }

    /**
     * Start process with constant memory load
     *  
     * @param logEvery period of reporting, in ms
     */
    public static Thread startConstantMemoryLoad( int logEvery ) {
        Runnable loadTask = () -> {

            final long sleepBetweenIterations = 10;

            final Logger LOG = LogManager.getLogger("jvmtests.csv");
            long beginTime = System.currentTimeMillis();
            long logCount = 0l;

            final Random random = new Random(0);
            List<String> report;

            final int listSizeMin = 100;
            final double listSizeDev = 100d;
            int listSize = 0;

            final int stringSizeMin = 10;
            final double stringSizeDev = 1000d;
            int stringSize = 0;

            double consumedValue = 0d;
            while ( !Thread.interrupted() ) {

                // Generate list of random-length strings
                report = new ArrayList<>();
                listSize = (int)( Math.log(1-random.nextDouble())*(-listSizeDev) )+listSizeMin;
                // listSize = Math.abs( (int) ( (Math.pow(random.nextGaussian(),1)*listSizeDev) + listSizeMin ) )+1;
                for ( int i = 0 ; i < listSize; i++ ) {
                    stringSize = (int)( Math.log(1-random.nextDouble())*(-stringSizeDev) )+stringSizeMin;
                    // stringSize = Math.abs( (int) ( (Math.pow(random.nextGaussian(),1)*stringSizeDev) + stringSizeMin ) )+1;
                    String str = "test";
                    str = str.repeat(stringSize);
                    report.add(str);
                    consumedValue += stringSize;
                }

                // Make some calculations
                Double dummyValue = 1d;
                for ( String str : report ) {
                    dummyValue *= ( (str.hashCode() % 10000) * 0.1 ) ;
                }

                // Sleep
                try {
                    Thread.sleep(sleepBetweenIterations);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Log statistics
                long startTime = System.currentTimeMillis() - beginTime;
                if ( startTime - logEvery * logCount > logEvery ) {
                    logCount = ( startTime/logEvery );
                    LOG.info( new ObjectArrayMessage( "constant load", startTime, consumedValue ) );
                    consumedValue = 0d;
                }
            }
            return;
        };
        Thread thread = new Thread(loadTask);
        thread.start();
        return thread;
    }

}


