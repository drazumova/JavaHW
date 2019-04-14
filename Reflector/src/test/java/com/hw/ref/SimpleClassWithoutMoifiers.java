package com.hw.ref;


class SimpleClassWithoutMoifiers extends Clazz implements MyInterface{
    private int intField;
    public char charField;
    long longField;
    static int staticIntField;
    private final int finalField;

    static {
        staticIntField = 42;
    }

    {
        finalField = 20;
    }

    SimpleClassWithoutMoifiers() {
        intField = 1;
        charField = '2';
        longField = 10;
    }

    public void print() {
        System.out.println("Kek");
    }

    private int print(int a) {
        System.out.println("Lol");
        return a;
    }

    String print(String a) {
        return "Arbidol " + a;
    }

    private static class NestedSimpleClass {
        final int resault = 42;

        void print(int a) throws Exception{
            System.out.println(resault);
            throw new Exception("TEst");
        }
    }
}
