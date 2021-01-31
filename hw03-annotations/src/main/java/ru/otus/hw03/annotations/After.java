package ru.otus.hw03.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Batch( level = 300
      , reusable = true
      , acceptableException = Throwable.class
      )
public @interface After {
}
