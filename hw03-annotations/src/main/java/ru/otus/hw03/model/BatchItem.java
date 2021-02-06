package ru.otus.hw03.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

import ru.otus.hw03.annotations.Batch;
import ru.otus.hw03.annotations.Title;

/**
 * Batch method wrapper with metadata and execution state.
 *
 */
public class BatchItem
{

    // Initial entry information
    final private Batch metadata;
    // Method to be executed
    final private Method method;

    // Title
    final private String title;

    // Execution status
    private boolean itemPassed = false;
    private Instant startedAt = null;
    private Instant finishedAt = null;
    private Throwable thrownException = null;

    /**
     * Initialize batch item with method. 
     * Method should have a @Batch annotation 
     * and should not take any arguments.
     *
     * @param method to be executed
     */
    public BatchItem(Method method)
    {
        this.method = method;
        this.metadata = findBatchAnnotation( method.getAnnotations(), 1 );
        String title = findTitle( method.getAnnotations() );
        this.title = title == null ? method.getName() : title;
    }

    /**
     * Execute batch item method on object.
     *
     * @param object an object to be examined
     * @return is execution passed successfully?
     */
    public boolean run(Object object) {
        // System.out.println("Working with "+object.hashCode() + " " + this.getTitle());
        if ( ! isValid() ) {
            return false;
        }
        boolean passed = false;
        Instant startedAt = null;
        Instant finishedAt = null;
        Throwable thrownException = null;
        try {
            startedAt = Instant.now();
            this.method.invoke(object);
            finishedAt = Instant.now();
            passed = true;
        } catch ( Exception e ){
            // System.out.print(e.getCause());
            finishedAt = Instant.now();
            thrownException = e.getCause();
            if ( this.metadata.acceptableException().isAssignableFrom(e.getCause().getClass()) ) {
                passed = true;
            }
            else {
                passed = false;
            }
        }
        if ( !this.metadata.reusable() ) {
            this.itemPassed = passed;
            this.startedAt = startedAt;
            this.finishedAt = finishedAt;
            this.thrownException = thrownException;
        }
        return passed;
    }


    /**
     * Checks that object is ready to use.
     *
     * @return false if it is not a valid batch item
     */
    public boolean isValid() {
        return metadata == null || method.getParameterCount() > 0 ? false : true;
    }

    public boolean isItemPassed() {
        return itemPassed;
    }

    public boolean isReusable() {
        if ( isValid() ) {
            return metadata.reusable();
        }
        else {
            return false;
        }
    }

    public boolean isNewInstanceLevel() {
        if ( isValid() ) {
            return metadata.newInstanceLevel();
        }
        else {
            return false;
        }
    }

    public String getExecDuration() {
        String result = null;
        if ( !isReusable() ) {
            if ( this.finishedAt != null && this.startedAt != null ) {
                result = String.valueOf( Duration.between(this.startedAt, this.finishedAt).toMillis() );
            }
        }
        return result;
    }

    public Throwable getThrownException() {
        return thrownException;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        if ( isValid() ) {
            return metadata.level();
        }
        else {
            return -1;
        }
    }

    /**
     * Finds @Batch annotation.
     *
     * @param annotations array of annotations
     * @param searchDepth number of levels of annotation annotations to be checked.
     * 0 - for direct annotations.
     * @return first occured @Batch annotation reference
     */
    private Batch findBatchAnnotation( Annotation[] annotations, int searchDepth ) {
        Batch result = null;
        for (int i = 0; i < annotations.length && result == null; i++)  {
            Annotation annotation = annotations[i];
            if ( annotation.annotationType().equals( Batch.class ) )  {
                result = (Batch) annotation;
            }
            else if ( searchDepth > 0 ) {
                result = findBatchAnnotation( annotation.annotationType().getAnnotations(), searchDepth-1 );
            }
        }
        return result;
    }

    /**
     * Finds @Title annotation.
     *
     * @param annotations array of annotations
     * @return string value of annotation
     */
    private String findTitle( Annotation[] annotations ) {
        String result = null;
        for (int i = 0; i < annotations.length && result == null; i++)  {
            Annotation annotation = annotations[i];
            if ( annotation.annotationType().equals( Title.class ) )  {
                result = ((Title)annotation).value();
            }
        }
        return result;
    }

}


