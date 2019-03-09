package com.hw.ref;

import org.junit.jupiter.api.*;
import com.hw.ref.SimpleClass;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ReflectorTest {

    @Test
    void simpleDiffTest() throws IOException {
        Reflector.diffClasses(SimpleClass.class, SimpleClass.class);
        var list = Files.readAllLines(Paths.get("diff"));
        assertTrue(list.isEmpty());
    }

    @Test
    void diffWtihEmptyClassTest() throws IOException {
        Reflector.diffClasses(SimpleClass.class, ComplexClass.class);
        var list = Files.readAllLines(Paths.get("diff"));
        for (var s : list) {
            System.out.println(s);
        }
        assertEquals(List.of("long com.hw.ref.SimpleClass.longField",
                "public char com.hw.ref.SimpleClass.charField",
                "private int com.hw.ref.SimpleClass.intField",
                "static int com.hw.ref.SimpleClass.staticIntField",
                "private final int com.hw.ref.SimpleClass.finalField",
                "",
                "java.lang.String com.hw.ref.SimpleClass.print(java.lang.String)",
                "private int com.hw.ref.SimpleClass.print(int)",
                "public void com.hw.ref.SimpleClass.print()"), list);
    }

    @Test
    void diffWithClassWithoutModifiersTest() throws IOException {
        Reflector.diffClasses(SimpleClass.class, SimpleClassWithoutMoifiers.class);
        var list = Files.readAllLines(Paths.get("diff"));
        assertEquals(List.of(""), list);
    }

    @Test
    void diffWithDifferentModifiersTest() throws IOException {
        Reflector.diffClasses(NewClass.class, AnotherClass.class);
        var list = Files.readAllLines(Paths.get("diff"));
        assertEquals(List.of("private int com.hw.ref.NewClass.field",
                            "public int com.hw.ref.AnotherClass.field",
                                ""), list);
    }

    @Test
    void diffInStaticTest() throws IOException {
        Reflector.diffClasses(AnotherClassWtihStaticField.class, AnotherClass.class);
        var list = Files.readAllLines(Paths.get("diff"));
        assertEquals(List.of("public static int com.hw.ref.AnotherClassWtihStaticField.field",
                            "public int com.hw.ref.AnotherClass.field",
                            ""), list);
    }

    @Test
    void diffAdditionMethodTest() throws IOException {
        Reflector.diffClasses(NewClassWithOneMethod.class, NewClass.class);
        var list = Files.readAllLines(Paths.get("diff"));
        assertEquals(List.of("",
                "private void com.hw.ref.NewClassWithOneMethod.method()"), list);
    }

    @Test
    void diffAdditionExceptionTest() throws IOException {
        Reflector.diffClasses(NewClassWithOneMethod.class, NewClassWithOneMethodWithException.class);
        var list = Files.readAllLines(Paths.get("diff"));
        assertEquals(List.of("",
                "private void com.hw.ref.NewClassWithOneMethod.method()",
                "private void com.hw.ref.NewClassWithOneMethodWithException.method() throws java.lang.Exception"), list);
    }
}