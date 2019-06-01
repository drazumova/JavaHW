package com.hw.junit.testsrc;


import com.hw.junit.annotaion.*;

public class TestExpected {

    @Test(expected = NullPointerException.class)
    void test() {
        throw new NullPointerException();
    }
}
