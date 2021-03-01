package ru.otus.hw04;

import ru.otus.hw04.gc.GcLogger;
import ru.otus.hw04.gc.JvmTests;

public class GcDemo {

    public static void main(String... args) 
        throws Exception
    {
        // log GC statistics every n ms
        GcLogger gcLogger = new GcLogger(10000);
        gcLogger.startMonitoring();

        // start aside meamory consumption and log its activity every n ms
        Thread constantLoadThread = JvmTests.startConstantMemoryLoad(10000);
        
        // exit with OutOfMemory after n ms
        Thread linearlyIncreasingLoadThread = JvmTests.startIncreasingMemoryLoad(300000);
        linearlyIncreasingLoadThread.join();
        constantLoadThread.stop();
    }

}


