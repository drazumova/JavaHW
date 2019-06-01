package com.hw.junit.annotaion;

import com.hw.junit.*;

import java.lang.annotation.*;

/**
 * Annotation for tests.
 * Has expected parameter for exception class that expected to bw thrown.
 * Has ignore parameter for reason not to run test.
 * Ignored test wont run
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {

    String EMPTY_VALUE = "NULL" +
            "    /\\_____/\\\n" +
            "   /  o   o  \\\n" +
            "  ( ==  ^  == )\n" +
            "   )         (\n" +
            "  (           )\n" +
            " ( (  )   (  ) )\n" +
            "(__(__)___(__)__)";

    Class<? extends Throwable> expected() default EmptyException.class;
    String ignore() default EMPTY_VALUE;
}