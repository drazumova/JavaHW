package com.hw.junit.annotaion;

import java.lang.annotation.*;


/**
 * Annotation for methods that will run before each test in the class
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
}