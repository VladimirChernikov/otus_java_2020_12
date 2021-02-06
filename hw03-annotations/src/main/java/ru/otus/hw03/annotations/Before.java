package ru.otus.hw03.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Batch( level = 100
      , reusable = true
      , newInstanceLevel = true
      )
public @interface Before {
}
