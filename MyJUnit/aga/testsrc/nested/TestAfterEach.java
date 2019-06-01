package aga.testsrc.nested;

import com.hw.junit.annotaion.*;

public class TestAfterEach {
    int b = 10;
    int cnt;

    @After
    public void clear() {
        b = 10;
        System.out.println(cnt);
        cnt++;
    }

    @Test
    public void test1() {
        if (b != 10) {
            throw new NullPointerException();
        }
        b = 9;
    }

    @Test
    public void test2() {
        if (b != 10) {
            throw new NullPointerException();
        }
        b = 9;
    }

    @Test
    void test3() {
        if (b != 10) {
            throw new NullPointerException();
        }
        b = 9;
    }

    @Test
    void test4() {
        if (b != 10) {
            throw new NullPointerException();
        }
        b = 9;
    }

    @Test
    void test5() {
        if (b != 10) {
            throw new NullPointerException();
        }
        b = 9;
    }
}
