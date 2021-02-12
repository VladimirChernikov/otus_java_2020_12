package ru.otus.hw04;

import ru.otus.hw04.gc.JvmTests;
import ru.otus.hw04.gc.GcLogger;

public class GcDemo {

    public static void main(String... args) 
        throws Exception
    {
        // log statistics every 60 seconds
        GcLogger gcLogger = new GcLogger(60000);
        gcLogger.startMonitoring();

        // exit with OutOfMemory after 300 seconds
        JvmTests.runOutOfMemoryProcess(300000);
    }

}


