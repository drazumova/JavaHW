package aga.testsrc;

import com.hw.junit.annotaion.*;

public class TestBeforeAfterClass {

    private static int cnt = 0;

    @BeforeClass
    static public void add() {
        if (cnt != 0) {
            throw new NullPointerException();
        }
    }

    @Test
    private void firstTest() {
        cnt++;
    }

    @AfterClass
    static public void check() {
        if (cnt != 1) {
            throw new NullPointerException();
        }
    }

}