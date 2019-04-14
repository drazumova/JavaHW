package com.hw.ref;

import org.jetbrains.annotations.*;


public class SimpleGenericClass<E, T> extends Clazz implements MyInterface{
    private @NotNull int intField;
    public char charField;
    long longField;
    static int staticIntField;
    private final int finalField;
    public E field;

    static {
        staticIntField = 42;
    }

    {
        finalField = 20;
    }

    SimpleGenericClass() {
        intField = 1;
        charField = '2';
        longField = 10;
    }

    protected SimpleGenericClass(T a) {
        intField = 1;
        charField = '2';
        longField = 10;
    }

    @Override
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

    private <U> T print(U a) {
        return null;
    }

    private static class NestedSimpleClass {
        private final int resault = 42;

        public void print(int a) throws Exception{
            System.out.println(resault);
            throw new Exception("TEst");
        }
    }
}