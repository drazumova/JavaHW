package aga.testsrc.nested;

import com.hw.junit.annotaion.*;

public class TestBefore {
    public static int a = 1;

    @BeforeClass
    static private void lel() {
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
