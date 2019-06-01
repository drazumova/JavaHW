package com.hw.junit.testsrc;

import com.hw.junit.annotaion.*;

public class TestBeforeAfter {

    private int cnt = 0;

    @Before
    void add() {
        if (cnt != 0) {
            throw new NullPointerException();
        }
    }

    @Test
    void firstTest() {
        cnt++;
    }

    @After
    void check() {
        if (cnt != 1) {
            throw new NullPointerException();
        }
    }

}
