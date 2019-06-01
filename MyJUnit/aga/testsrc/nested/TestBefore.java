package aga.testsrc.nested;

import com.hw.junit.annotaion.*;

public class TestBefore {
    private int a = 1;

    @BeforeClass
    void lel() {
        System.out.println("KEK");
        a = 2;
    }

    @Test
    public void kek() {
        System.out.println(a);
        if (a != 2) {
            throw new NullPointerException();
        }
    }
}
