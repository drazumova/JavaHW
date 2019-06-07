package com.hw.junit.annotaion;

import java.lang.annotation.*;


/**
 * Annotation for methods that will run after all tests in the class
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AfterClass {
}