package aga.testsrc;

import com.hw.junit.annotaion.*;

public class TestBeforeAfter {

    private int cnt = 0;

    @Before
    public void add() {
        if (cnt != 0) {
            throw new NullPointerException();
        }
    }

    @Test
    private void firstTest() {
        cnt++;
    }

    @After
    public void check() {
        if (cnt != 1) {
            throw new NullPointerException();
        }
    }

}
