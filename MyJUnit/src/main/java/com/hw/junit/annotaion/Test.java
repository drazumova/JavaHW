package com.hw.junit.annotaion;

import com.hw.junit.*;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    String EMPTY_VALUE = "NULL";

    Class<? extends Throwable> expected() default EmptyException.class;
    String ignore() default EMPTY_VALUE;
}