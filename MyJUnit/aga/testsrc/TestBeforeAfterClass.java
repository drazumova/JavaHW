package aga.testsrc;

import com.hw.junit.annotaion.*;

public class TestBeforeAfterClass {

    private int cnt = 0;

    @BeforeClass
    public void add() {
        if (cnt != 0) {
            throw new NullPointerException();
        }
    }

    @Test
    private void firstTest() {
        cnt++;
    }

    @AfterClass
    public void check() {
        if (cnt != 1) {
            throw new NullPointerException();
        }
    }

}