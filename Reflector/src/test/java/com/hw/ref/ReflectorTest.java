package com.hw.ref;

import org.junit.jupiter.api.*;
import com.hw.ref.SimpleClass;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectorTest {

    @Test
    void complexGenericClassPrintAndDiffTest() throws IOException, ClassNotFoundException, InterruptedException {
        Reflector.printStructure(SimpleGenericClass.class);
        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, "src/test/java/com/hw/ref/SomeClass.java");
        var classLoad = SimpleGenericClass.class.getClassLoader();
        classLoad.loadClass("com.hw.ref.SomeClass");

        Reflector.diffClasses(SimpleGenericClass.class, SomeClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of(""), list);
    }

    @Test
    void printEmptyClassTest() throws IOException {
        Reflector.printStructure(ComplexClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileName));
        var correctAnswer = Files.readAllLines(Paths.get("src/test/answers/ComplexClassPrinted"));
        assertEquals(correctAnswer, list);
    }

    @Test
    void printSimpleClassTest() throws IOException {
        Reflector.printStructure(SimpleClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileName));
        var correctAnswer = Files.readAllLines(Paths.get("src/test/answers/SimpleClassPrinted"));
        assertEquals(correctAnswer, list);
    }

    @Test
    void printGenericTest() throws IOException {
        Reflector.printStructure(GenericClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileName));
        var correctAnswer = Files.readAllLines(Paths.get("src/test/answers/GenericClassPrinted"));
        assertEquals(correctAnswer, list);
    }

    @Test
    void printGenericWtihSuperClassAndInterfaceTest() throws IOException {
        Reflector.printStructure(NextGenericClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileName));
        var correctAnswer = Files.readAllLines(Paths.get("src/test/answers/GenericMethodsClassPrinted"));
        assertEquals(correctAnswer, list);
    }

    @Test
    void printMethodWtihExceptionTest() throws IOException {
        Reflector.printStructure(NewClassWithOneMethodWithException.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileName));
        var correctAnswer = Files.readAllLines(Paths.get("src/test/answers/MethodWithExceptionPrinted"));
        assertEquals(correctAnswer, list);
    }

    @Test
    void printInnerInterfaceTest() throws IOException {
        Reflector.printStructure(ClassWithInterface.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileName));
        var correctAnswer = Files.readAllLines(Paths.get("src/test/answers/ClassWithInterfacePrinted"));
        assertEquals(correctAnswer, list);
    }

    @Test
    void simpleDiffTest() throws IOException {
        Reflector.diffClasses(SimpleClass.class, SimpleClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of(""), list);
    }

    @Test
    void diffWtihEmptyClassTest() throws IOException {
        Reflector.diffClasses(SimpleClass.class, ComplexClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of("long com.hw.ref.SimpleClass.longField",
        "public char com.hw.ref.SimpleClass.charField",
        "private int com.hw.ref.SimpleClass.intField",
        "static int com.hw.ref.SimpleClass.staticIntField",
        "final int com.hw.ref.Clazz.finalInt",
        "private final int com.hw.ref.SimpleClass.finalField",
        "",
        "java.lang.String com.hw.ref.SimpleClass.print(java.lang.String)",
        "private int com.hw.ref.SimpleClass.print(int)",
        "public void com.hw.ref.SimpleClass.print()"), list);
    }

    @Test
    void diffWithClassWithoutModifiersTest() throws IOException {
        Reflector.diffClasses(SimpleClass.class, SimpleClassWithoutMoifiers.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of(""), list);
    }

    @Test
    void diffWithDifferentModifiersTest() throws IOException {
        Reflector.diffClasses(NewClass.class, AnotherClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of("private int com.hw.ref.NewClass.field",
                            "public int com.hw.ref.AnotherClass.field",
                                ""), list);
    }

    @Test
    void diffInStaticTest() throws IOException {
        Reflector.diffClasses(AnotherClassWtihStaticField.class, AnotherClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of("public static int com.hw.ref.AnotherClassWtihStaticField.field",
                            "public int com.hw.ref.AnotherClass.field",
                            ""), list);
    }

    @Test
    void diffAdditionMethodTest() throws IOException {
        Reflector.diffClasses(NewClassWithOneMethod.class, NewClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of("",
                "private void com.hw.ref.NewClassWithOneMethod.method()"), list);
    }

    @Test
    void diffParentFieldTest() throws IOException {
        Reflector.diffClasses(ChildClass.class, ComplexClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of("private int com.hw.ref.NewClass.field",
                            ""), list);
    }

    @Test
    void diffAdditionExceptionTest() throws IOException {
        Reflector.diffClasses(NewClassWithOneMethod.class, NewClassWithOneMethodWithException.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of("",
                "private void com.hw.ref.NewClassWithOneMethod.method()",
                "private void com.hw.ref.NewClassWithOneMethodWithException.method() throws java.lang.Exception"), list);
    }

    @Test
    void diffGenericTest() throws IOException {
        Reflector.diffClasses(GenericClass.class, AnotherGenericClass.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of(""), list);
    }

    @Test
    void diffGenericExtendsObjectTest() throws IOException {
        Reflector.diffClasses(GenericClass.class, GenericClassExtendsObject.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of(""), list);
    }

    @Test
    void diffGenericExtendsIntegerTest() throws IOException {
        Reflector.diffClasses(GenericClass.class, GenericClassExtendsInteger.class);
        var list = Files.readAllLines(Paths.get(Reflector.fileDifference));

        assertEquals(List.of("private java.lang.Object com.hw.ref.GenericClass.field",
                            "private java.lang.Integer com.hw.ref.GenericClassExtendsInteger.field",
                            ""), list);
    }
}