package com.hw.ref;

public class ComplexClass {

}

class NewClass {
    private int field;
}

class ChildClass extends NewClass {

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

interface GenericInterface<E> {
    void print(E o);
}

class NextGenericClass<E> extends GenericClass<E> implements GenericInterface<E>, MyInterface {

    NextGenericClass(int a){
        System.out.println(a);
    }

    @Override
    public void print(E o){

    }

    @Override
    public void print() {

    }

    private <U, T> T print(U o, T a) {
        return a;
    }
}


class ClassWithInterface {
    private interface InnerInterface extends MyInterface, CoolInterface{
        int field = 42;
    }
}