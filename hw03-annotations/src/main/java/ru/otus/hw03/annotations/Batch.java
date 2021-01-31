package ru.otus.hw03.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;

/**
 * Base method annotation for batch execution thread.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Batch {

    // level determines the execution order
    int level();

    // reusable are multiple times executed, read-only items 
    // without statistics collection (may be suitable for: @Before, @After)
    // and not reusable also means to elevate execution to next level
    // (instead of the next execution of the other item within the same level)
    boolean reusable();

    // newInstance forces to create a new instance of the examined class just 
    // before the level entrance
    boolean newInstanceLevel() default false;

    // acceptableException indicates that this exception (and extended ones) 
    // should be ignored
    Class<? extends Throwable> acceptableException() default InvocationTargetException.class; // TODO: replace with some dummy class

}
