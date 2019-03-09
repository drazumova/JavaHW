package com.hw.ref;

public class ComplexClass {

}

class NewClass {
    private int field;
}

class AnotherClass {
    public int field;
}

class AnotherClassWtihStaticField {
    public static int field;
}

class NewClassWithOneMethod {
    private int field;

    private void method() {}

}

class NewClassWithOneMethodWithException {
    private int field;

    private void method() throws Exception {
        throw new Exception("AAAAAAAAAa");
    }

}

class GenericClass<E> {
    private E field;
}

class AnotherGenericClass<U> {
    private U field;
}

class GenericClassExtendsObject<U extends Object> {
    private U field;
}

class GenericClassExtendsInteger<E extends Integer> {
    private E field;
}