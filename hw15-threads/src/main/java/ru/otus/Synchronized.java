package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Synchronized {
    private static final Logger logger = LoggerFactory.getLogger(Synchronized.class);

    private final Object monitor = new Object();
    private Integer currentThreadNumber; 
    private Integer maxThreadNumber; 

    public Synchronized(Integer currentThreadNumber, Integer maxThreadNumber) {
        this.currentThreadNumber = currentThreadNumber;
        this.maxThreadNumber = maxThreadNumber;
    }

    private int nextThread(int currentThreadNumber) {
        currentThreadNumber ++;
        return currentThreadNumber %= this.maxThreadNumber;
    }

    private void action(int threadNumber) {
        try {
            int threadValue = 1;
            int incr = 1;
            int maxValue = 10;
            while (true) {
                synchronized( this.monitor )
                {
                    while ( ! this.currentThreadNumber.equals(threadNumber) ) {
                        this.monitor.wait();
                    }

                    logger.info("Thread {}: {}", threadNumber, threadValue);
                    currentThreadNumber = this.nextThread( currentThreadNumber );
                    this.monitor.notifyAll();

                }
                sleep();
                threadValue += incr;
                if ( threadValue % maxValue == 0 )  {
                    incr *= -1;
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new NotInterestingException(ex);
        }
    }

    public static void main(String[] args) {
        Synchronized synchro = new Synchronized(0, 2);
        new Thread(() -> synchro.action(0)).start();
        new Thread(() -> synchro.action(1)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static class NotInterestingException extends RuntimeException {
        NotInterestingException(InterruptedException ex) {
            super(ex);
        }
    }

}
