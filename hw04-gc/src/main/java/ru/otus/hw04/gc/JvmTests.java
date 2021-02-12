package ru.otus.hw04.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to test JVM
 *
 */
public class JvmTests
{

    /**
     * Produce an OutOfMemory
     *  
     * @param crashAfter seconds till expected OutOfMemory
     */
    public static void runOutOfMemoryProcess(final long crashAfter)
            throws Exception
        {
            // maxmimum heap memory -Xmx
            final long maxMemory = Runtime.getRuntime().maxMemory();
            // numberOfIterations till expected OOM
            final long numberOfIterations = 1000;
            final long step = ( maxMemory / numberOfIterations / 32 );
            // pause between itertaions
            final long delayValue = crashAfter / numberOfIterations;
            // System.out.println(maxMemory+" "+ numberOfIterations+" "+ delayValue);
            // dummy list of records
            final List<String> report = new ArrayList<>();
            int arraySize = 1;
            // final long beginTime = System.currentTimeMillis();
            for ( long iter = 1; iter <= numberOfIterations * 2; iter++ ) {
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
                Thread.sleep( delayValue );
                // System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
            }
        }
}


