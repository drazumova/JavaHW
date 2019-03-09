package com.hw.ref;

import org.jetbrains.annotations.*;

import javax.lang.model.element.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import static java.util.Arrays.sort;
import static java.util.stream.Collectors.joining;

public class Reflector {

    private static final String fileName2 = "testResault";
    private static final String fileName = "src/main/java/com/hw/ref/SomeClass.java";
    private static final String fileDifference = "diff";
    
    private static void printMethod(Method method, PrintWriter writer, String tabs) {
        String modifiers = Modifier.toString(method.getModifiers() + Modifier.ABSTRACT);
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }
        writer.print(tabs + modifiers + method.getReturnType().getName() + " " + method.getName() + "(");
        char name = 'a';
        for (var parameter : method.getParameterTypes()) {
            writer.print(parameter.getName() + " " + name);
            name++;
        }
        writer.print(")");
        var exceptionList = List.of(method.getExceptionTypes());
        if (!exceptionList.isEmpty()) {
            writer.print(" throws " + exceptionList.stream().map(Class::getName).collect( joining( ", " ) ));
        }

        writer.println(";");
    }

    private static void printConstructor(Constructor<?> constructor, PrintWriter writer, String tabs) {
        String modifiers = Modifier.toString(constructor.getModifiers() + Modifier.ABSTRACT);
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }

        writer.print(tabs + modifiers + constructor.getName() + "(");

        char name = 'a';
        for (var parameter : constructor.getParameterTypes()) {
            writer.print(parameter.getName() + " " + name);
            name++;
        }
        writer.print(")");
        var exceptionList = List.of(constructor.getExceptionTypes());
        if (!exceptionList.isEmpty()) {
            writer.print(" throws " + exceptionList.stream().map(Class::getName).collect( joining( ", " ) ));
        }

        writer.println(";");
    }
    
    private static void printField(Field field, PrintWriter writer, String tabs) {
        String modifiers = Modifier.toString(field.getModifiers());
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }

        writer.println(tabs + modifiers + field.getType() + " " + field.getName() + ";");
    }
    
    private static void printClass(Class<?> clazz, PrintWriter writer, String tabs) {
        var mod = clazz.getModifiers();
        var superClass = clazz.getSuperclass();
        String innerTabs = tabs + "    ";

        writer.print(tabs + Modifier.toString(mod) + " class " + clazz.getSimpleName() + " ");

        writer.print("extends " + superClass.getName());

        List<Class<?>> interfacesList = List.of(clazz.getInterfaces());
        if (!interfacesList.isEmpty()) {
            writer.print(" implements " + interfacesList.stream().map(Class::getName).collect( joining( ", " ) ));
        }

        writer.println(" {\n");

        for (var i : clazz.getDeclaredFields()) {
            printField(i, writer, innerTabs);
        }

        writer.println();

//        for (var i : clazz.getDeclaredConstructors()) {
//            printConstructor(i, writer, innerTabs);
//        }

        writer.println();

        for (var i : clazz.getDeclaredMethods()) {
            printMethod(i, writer, innerTabs);
        }

        writer.println();

        for (var i : clazz.getDeclaredClasses()) {
            printClass(i, writer, innerTabs);
        }

        writer.println(tabs + "}\n");
    }


    /**
     * Ignore final modifier,
     * @param someClass class to print
     */
    public static void printStructure(Class<?> someClass) throws IOException {
        try (var writer = new PrintWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            writer.println("package " + someClass.getPackageName() + ";\n");
            writer.print("abstract ");
            printClass(someClass, writer, "");
        }
    }

    private static void fieldsEquals(Class<?> first, Class<?> second, PrintWriter writer) {
        var comparator = Comparator.comparing((Field f) -> f.getModifiers()).thenComparing(f -> f.getType().toString());
        
        var firstFieldsSet = new TreeSet<>(comparator);
        firstFieldsSet.addAll(List.of(first.getDeclaredFields()));
        var secondFieldsSet = new TreeSet<>(comparator);
        secondFieldsSet.addAll(List.of(second.getDeclaredFields()));
        
        for (var field : firstFieldsSet) {
            if (!secondFieldsSet.contains(field)) {
                writer.println(field);
            }
        }

        for (var field : secondFieldsSet) {
            if (!firstFieldsSet.contains(field)) {
                writer.println(field);
            }
        }
    }

    private static String methodToString(Method method) {
        String resault = "";
        String modifiers = Modifier.toString(method.getModifiers());
        resault += modifiers + " " + method.getReturnType().getName() + " " + method.getName() + "(";

        var parameters = method.getParameterTypes();
        sort(parameters);
        for (var parameter : parameters) {
            resault += parameter.getName() + " ";
        }

        resault += ")";

        var exceptionList = method.getExceptionTypes();
        sort(exceptionList);
        resault += " throws " + List.of(exceptionList).stream().map(Class::getName).collect( joining( ", " ) );

        return resault;
    }

    private static void methodsEquals(Class<?> first, Class<?> second, PrintWriter writer) {
        var firstMethodsSet = new TreeSet<>(Comparator.comparing(Reflector::methodToString));
        firstMethodsSet.addAll(List.of(first.getDeclaredMethods()));
        var secondMethodsSet = new TreeSet<>(Comparator.comparing(Reflector::methodToString));
        secondMethodsSet.addAll(List.of(second.getDeclaredMethods()));


        for (var method : firstMethodsSet) {
            if (!secondMethodsSet.contains(method)) {
                writer.println(method);
            }
        }

        for (var method : secondMethodsSet) {
            if (!firstMethodsSet.contains(method)) {
                writer.println(method);
            }
        }
    }

    private static boolean classesEquals(Class<?> first, Class<?> second, PrintWriter writer) {
        var firstClassesList = Set.of(first.getDeclaredClasses());
        var secondClassesList = Set.of(second.getDeclaredClasses());

        for (var clazz : firstClassesList) {
            if (!secondClassesList.contains(clazz)) {
                printClass(clazz, writer, "");
            }
        }

        for (var clazz : secondClassesList) {
            if (!firstClassesList.contains(clazz)) {
                printClass(clazz, writer, "");
            }
        }

        return true;
    }

    private static void compareClasses(Class<?> first, Class<?> second, PrintWriter writer) {
        fieldsEquals(first, second, writer);
        writer.println();
        methodsEquals(first, second, writer);
//        classesEquals(first, second, writer);
    }

    /**
     * Create file "diff" with all different methods and fields in given classes/
     * @return is classes equal
     */
    public static void diffClasses(Class<?> a, Class<?> b) throws IOException {
        try (var writer = new PrintWriter(new FileWriter(fileDifference, StandardCharsets.UTF_8))) {
            compareClasses(a, b, writer);
        }
    }
}
