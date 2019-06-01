package com.hw.junit.testsrc;


import com.hw.junit.annotaion.*;

public class TestFailed {

    @Test
    void test() {
        throw new NullPointerException();
    }
}
