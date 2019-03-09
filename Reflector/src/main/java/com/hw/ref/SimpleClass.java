package com.hw.ref;

import org.jetbrains.annotations.*;

interface MyInterface {
    void print();
}

interface CoolInterface {
    Character a = 'a';
}

class Clazz {
    final int finalInt = 41;
}

public class SimpleClass extends Clazz implements MyInterface{
    private @NotNull int intField;
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

    SimpleClass() {
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

    private static class NestedSimpleClass {
        private final int resault = 42;

        public void print(int a) throws Exception{
            System.out.println(resault);
            throw new Exception("TEst");
        }
    }
}
