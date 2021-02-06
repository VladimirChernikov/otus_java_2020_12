package ru.otus.hw03;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

import ru.otus.hw03.model.BatchItem;

/**
 * DIY unit testing utility with minimal support for @Before, 
 * @Test, @After method annotations and reporting.
 *
 * @see ru.otus.hw03.annotations.Batch
 *
 */
public class DiyTester
{
    // A class which contains methods to be executed
    final private Class<?> examinedClass;
    // Collection of test methods (ordered by execution level)
    final private List<BatchItem> batchItems = new ArrayList<>();

    /**
     * Creates a new @{link DiyTester} instance bounded to specified class.
     *
     * @param className of the class which contains test methods
     * @throws ClassNotFoundException
     */
    public DiyTester(String className) throws ClassNotFoundException {
        // try to find class by name, throw an exception if none
        examinedClass = Class.forName(className);
        // and register class methods
        registerMethods( examinedClass.getMethods() );
    }

    /**
     * Starts all tests.
     * This method contains the main logic of execution thread formation
     * dependent on @Batch metadata.
     *
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     */
    public void runAllTests() 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException 
    {
        // mutatble list iterator of batchItems list
        ListIterator<BatchItem> li = (new ArrayList<>(this.batchItems)).listIterator();

        // initial values
        int skippedItems = 0;
        int currentLevel = Integer.MIN_VALUE;
        Queue<BatchItem> batchQueue = new LinkedList<>();
        // main loop
        while ( li.hasNext() )  
        {
            BatchItem batchItem = li.next();
            boolean foundNextLevel = false;
            // spawn thread if we are requred to create a new 
            // instance before the level entrance
            if ( batchItem.isNewInstanceLevel() 
                    && currentLevel != batchItem.getLevel()
                    && !batchQueue.isEmpty() 
               ) {
                spawnThread(batchQueue);
                batchQueue = new LinkedList<>();
               }
            currentLevel = batchItem.getLevel();
            batchQueue.add(batchItem);

            // elevate level if item is not reusable
            if ( !batchItem.isReusable() ) {
                li.remove(); // remove non reusable item
                while ( li.hasNext() && !foundNextLevel )  {
                    if ( li.next().getLevel() != currentLevel ) {
                        li.previous();
                        foundNextLevel = true;
                    } else {
                        skippedItems++;
                    }
                }
            }

            // if at the end
            // and there are skipped items behind
            // rewind back
            if ( !li.hasNext() && skippedItems > 0 ) {
                skippedItems = 0;
                while ( li.hasPrevious() ) {
                    li.previous();
                }
            }
        }
        // spawn remaining thread
        if ( !batchQueue.isEmpty() ) {
            spawnThread(batchQueue);
        }
    }

    /**
     * Prints test execution report to console.
     *
     * Example output:
     * EXAMINED CLASS: ru.otus.hw03.tests.SomeTests
     * 	TEST: Сегодня понедельник?
     * 		duration: 19 ms
     * 		execution status: FAILED with java.lang.Exception exception
     * 	TEST: Рекурсия
     * 		duration: 4 ms
     * 		execution status: FAILED with java.lang.StackOverflowError exception
     * 	TEST: Доступ к массиву
     * 		duration: 0 ms
     * 		execution status: FAILED with java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 3 exception
     * 	TEST: Конкатенация строки
     * 		duration: 1163 ms
     * 		execution status: FAILED with java.lang.OutOfMemoryError: Overflow: String length out of range exception
     * 	TEST: Вывод строки
     * 		duration: 0 ms
     * 		execution status: PASSED
     * SUMMARY: 1/5 tests are PASSED
     *
     */
    public void report() {
        System.out.println("EXAMINED CLASS: "+ this.examinedClass.getName());
        int totalCount = 0;
        int passedCount = 0;
        for ( BatchItem batchItem : batchItems ) {
            if ( !batchItem.isReusable() ) {
                if ( batchItem.isItemPassed() ) {
                    passedCount++;
                }
                totalCount++;
                System.out.println("\tTEST: " + batchItem.getTitle() + "\n\t\tduration: "+ batchItem.getExecDuration() + " ms" + "\n\t\texecution status: "+ ( batchItem.isItemPassed() ? "PASSED" : "FAILED with "+ batchItem.getThrownException() +" exception" ));
            }
        }
        System.out.println("SUMMARY: " + passedCount + "/" + totalCount + " tests are PASSED");
    }

    /**
     * Populates batchItems collection.
     *
     * @param methods to be registered
     */
    private void registerMethods( Method[] methods ) 
    {
        for ( Method method : methods )  {
            BatchItem batchItem = new BatchItem(method);
            if ( batchItem.isValid() ) {
                batchItems.add(batchItem);
            } 
        }
        batchItems.sort( Comparator.comparing( BatchItem::getLevel ) );
    }


    /**
     * Spawns execution thread.
     *
     * @param batchQueue
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    private void spawnThread( Queue<BatchItem> batchQueue ) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        // instance of examined class
        final Object object = this.examinedClass.getDeclaredConstructor().newInstance();
        // execute batch methods till the first fail
        while ( !batchQueue.isEmpty() && batchQueue.remove().run(object) ) {
        }
    }


}


