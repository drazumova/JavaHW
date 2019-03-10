package com.hw.ref;

import org.jetbrains.annotations.*;

import javax.lang.model.element.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

/**
 * Class allows compare and print structure of classes.
 */
public class Reflector {
    public static final String fileName = "src/test/java/com/hw/ref/SomeClass.java";
    public static final String fileDifference = "diffClassesResault";

    private static void printMethod(Method method, PrintWriter writer, String tabs) {
        String modifiers = Modifier.toString(method.getModifiers());
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }

        String genericParameters = "";
        if (method.getTypeParameters().length != 0) {
            genericParameters = "<" + List.of(method.getTypeParameters())
                    .stream()
                    .map(TypeVariable::getName)
                    .collect(Collectors.joining(", ")) + "> ";
        }

        writer.print(tabs + modifiers + genericParameters + method.getGenericReturnType().getTypeName() + " " + method.getName() + "(");
        char name = 'a';
        var parametersList = new ArrayList<String>();
        for (var parameter : method.getGenericParameterTypes()) {
            parametersList.add(parameter.getTypeName() + " " + name);
            name++;
        }

        writer.print(parametersList.stream().collect( Collectors.joining( ", " ) ));
        writer.print(")");
        var exceptionList = List.of(method.getGenericExceptionTypes());
        if (!exceptionList.isEmpty()) {
            writer.print(" throws " + exceptionList.stream().map(Type::getTypeName).collect( Collectors.joining( ", " ) ));
        }

        writer.println(";");
    }

    private static void printConstructor(Constructor<?> constructor, PrintWriter writer, String tabs, String className) {
        String modifiers = Modifier.toString(constructor.getModifiers());
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }

        writer.print(tabs + modifiers + className + "(");

        char name = 'a';
        for (var parameter : constructor.getGenericParameterTypes()) {
            writer.print(parameter.getTypeName() + " " + name);
            name++;
        }
        writer.print(")");
        var exceptionList = List.of(constructor.getGenericExceptionTypes());
        if (!exceptionList.isEmpty()) {
            writer.print(" throws " + exceptionList.stream()
                    .map(Type::getTypeName)
                    .collect( Collectors.joining( ", " ) ));
        }

        writer.println(";");
    }

    private static void printField(Field field, PrintWriter writer, String tabs) {
        String modifiers = Modifier.toString(field.getModifiers());
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }
        writer.println(tabs + modifiers + field.getGenericType() + " " + field.getName() + ";");
    }

    private static String printGenericType(Class<?> clazz) {
        var list = List.of(clazz.getTypeParameters());
        if (list.isEmpty()) {
            return "";
        }

        return "<" +list.stream().map(TypeVariable::getName).collect( Collectors.joining(", ")) + ">";
    }

    private static void printClassHead(Class<?> clazz, PrintWriter writer, String tabs, String className){
        if (clazz.isInterface()) {
            var mod = clazz.getModifiers();
            var superClass = clazz.getGenericSuperclass();
            writer.print(tabs + Modifier.toString(mod) + " " + className + printGenericType(clazz) + " ");

            List<Type> interfacesList = List.of(clazz.getGenericInterfaces());
            if (!interfacesList.isEmpty()) {
                writer.print(" extends " + interfacesList.stream()
                        .map(Type::getTypeName)
                        .collect( Collectors.joining( ", " ) ));
            }
        } else {
            var mod = clazz.getModifiers();
            var superClass = clazz.getGenericSuperclass();
            writer.print(tabs + Modifier.toString(mod) + " class " + className + printGenericType(clazz) + " ");

            writer.print("extends " + superClass.getTypeName());

            List<Type> interfacesList = List.of(clazz.getGenericInterfaces());
            if (!interfacesList.isEmpty()) {
                writer.print(" implements " + interfacesList.stream()
                        .map(Type::getTypeName)
                        .collect( Collectors.joining( ", " ) ));
            }
        }
    }

    private static void printClass(Class<?> clazz, PrintWriter writer, String tabs, String className) {
        String innerTabs = tabs + "    ";
        printClassHead(clazz, writer, tabs, className);

        writer.println(" {");

        for (var item : clazz.getDeclaredFields()) {
            printField(item, writer, innerTabs);
        }

        writer.println();

        for (var item : clazz.getDeclaredConstructors()) {
            printConstructor(item, writer, innerTabs, className);
        }

        writer.println();

        for (var item : clazz.getDeclaredMethods()) {
            printMethod(item, writer, innerTabs);
        }

        writer.println();

        for (var item : clazz.getDeclaredClasses()) {
            printClass(item, writer, innerTabs, item.getSimpleName());
        }

        writer.println(tabs + "}");
    }

    /**
     * Prints given class structure. Methods and constructors printed without implementation.
     * Saves all modifiers from fields, methods and classes.
     * Due to absense of implementation in methods and contrustors recieved file can not be builded,
     * because all classes have at least one constructor.
     * @param someClass class to print
     */
    public static void printStructure(Class<?> someClass) throws IOException {
        try (var writer = new PrintWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            writer.println("package " + someClass.getPackageName() + ";\n");
            printClass(someClass, writer, "", "SomeClass");
        }
    }

    private static Set<Field> getFieldsSet(Class<?> clazz) {
        var comparator = Comparator.comparing(Field::getModifiers)
                .thenComparing(f -> f.getType()
                        .toString());

        var fieldsSet = new TreeSet<>(comparator);

        var currentClass = clazz;
        while (!currentClass.equals(Object.class)) {
            fieldsSet.addAll(List.of(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }
        return fieldsSet;
    }

    private static void fieldsEquals(Class<?> first, Class<?> second, PrintWriter writer) {
        var firstFieldsSet = getFieldsSet(first);
        var secondFieldsSet = getFieldsSet(second);
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
        Arrays.sort(parameters);
        for (var parameter : parameters) {
            resault += parameter.getName() + " ";
        }

        resault += ")";

        var exceptionList = method.getExceptionTypes();
        Arrays.sort(exceptionList);
        resault += " throws " + List.of(exceptionList)
                .stream()
                .map(Class::getName)
                .collect( Collectors.joining( ", " ) );

        return resault;
    }

    private static Set<Method> getMethodsSet(Class<?> clazz) {
        var currentClass = clazz;
        var fieldsSet = new TreeSet<>(Comparator.comparing(Reflector::methodToString));
        while (!currentClass.equals(Object.class)) {
            fieldsSet.addAll(List.of(currentClass.getDeclaredMethods()));
            currentClass = currentClass.getSuperclass();
        }
        return fieldsSet;
    }
    
    private static void methodsEquals(Class<?> first, Class<?> second, PrintWriter writer) {
        var firstMethodsSet = getMethodsSet(first);
        var secondMethodsSet = getMethodsSet(second);
        
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

    private static void compareClasses(Class<?> first, Class<?> second, PrintWriter writer) {
        fieldsEquals(first, second, writer);
        writer.println();
        methodsEquals(first, second, writer);
    }

    /**
     * Create file "diffClassesResault" with all different methods and fields in given classes.
     * In comparing methods order of types of parameters is matter.
     * Does not compare constructors.
     * Does not compare implemented interfaces.
     */
    public static void diffClasses(Class<?> a, Class<?> b) throws IOException {
        try (var writer = new PrintWriter(new FileWriter(fileDifference, StandardCharsets.UTF_8))) {
            compareClasses(a, b, writer);
        }
    }
}
