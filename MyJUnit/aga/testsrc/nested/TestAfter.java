package aga.testsrc.nested;

import com.hw.junit.annotaion.*;

public class TestAfter {

    @AfterClass
    private void printKek() {
        System.out.println("KEK");
    }

    @Test
    public void test() {}
}
