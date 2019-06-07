package aga.testsrc.nested;

import com.hw.junit.annotaion.*;

public class TestBeforeEach {

    private static volatile int a = 1;

    @Before
    public void lel() {
        System.out.println("KEK " + a);
        a = 2;
    }

    @Test
    public void kek1() {
        System.out.println(a);
        if (a != 2) {
            throw new NullPointerException();
        }
        a = 3;
    }

    @Test
    public void kek2() {
        if (a != 2) {
            throw new NullPointerException();
        }
        a = 3;
    }

    @Test
    public void kek3() {
        if (a != 2) {
            throw new NullPointerException();
        }
        a = 3;
    }

    @Test
    public void kek4() {
        if (a != 2) {
            throw new NullPointerException();
        }
        a = 3;
    }

    @Test
    public void kek5() {
        if (a != 2) {
            throw new NullPointerException();
        }
        a = 3;
    }

    @Test
    public void kek6() {
        if (a != 2) {
            throw new NullPointerException();
        }
        a = 3;
    }

    @Test
    public void kek7() {
        if (a != 2) {
            throw new NullPointerException();
        }
        a = 3;
    }



}
