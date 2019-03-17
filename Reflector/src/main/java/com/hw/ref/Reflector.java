package com.hw.ref;

import org.jetbrains.annotations.*;

import javax.lang.model.element.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.reflect.Modifier;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * Class allows compare and print structure of classes.
 */
public class Reflector {
    public static final String fileDifference = "diffClassesResault";

    private static String typeWithoutPackage(Type type) {
        var typeName = type.getTypeName();
        var parts = typeName.split(Pattern.quote("."));
        if (parts.length == 0) {
            return typeName;
        }
        return parts[parts.length - 1];
    }

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

        writer.print(tabs + modifiers + genericParameters +
                typeWithoutPackage(method.getGenericReturnType()) + " "
                + method.getName() + "(");

        var parameterList = new ArrayList<String>();

        var name = "arg";
        int counter = 0;
        for (var parameter : method.getGenericParameterTypes()) {
            parameterList.add(typeWithoutPackage(parameter) + " " + name + counter);
            counter++;
        }
        writer.print(String.join(", ", parameterList));

        writer.print(")");
        var exceptionList = List.of(method.getExceptionTypes());
        if (!exceptionList.isEmpty()) {
            writer.print(" throws " + exceptionList.stream().map(Class::getSimpleName).collect(Collectors.joining(", " )));
        }

        writer.println(" { throw new UnsupportedOperationException(); }");
    }

    private static void printConstructor(Constructor<?> constructor, PrintWriter writer, String tabs, String className) {
        String modifiers = Modifier.toString(constructor.getModifiers());
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }
        writer.print(tabs + modifiers + className + "(");

        var parameterList = new ArrayList<String>();

        var name = "arg";
        int counter = 0;
        for (var parameter : constructor.getGenericParameterTypes()) {
            parameterList.add(typeWithoutPackage(parameter) + " " + name + counter);
            counter++;
        }
        writer.print(String.join(", ", parameterList));

        writer.print(")");
        var exceptionList = List.of(constructor.getExceptionTypes());
        if (!exceptionList.isEmpty()) {
            writer.print(" throws " + exceptionList.stream()
                    .map(Class::getSimpleName)
                    .collect(Collectors.joining(", ")));
        }
        writer.println(" { throw new UnsupportedOperationException(); }");
    }

    private static void printField(Field field, PrintWriter writer, String tabs) {
        String modifiers = Modifier.toString(field.getModifiers());
        if (!modifiers.isEmpty()) {
            modifiers += " ";
        }
        writer.println(tabs + modifiers + typeWithoutPackage(field.getGenericType()) + " " + field.getName() + ";");
    }

    private static String printGenericType(Class<?> clazz) {
        var list = List.of(clazz.getTypeParameters());
        if (list.isEmpty()) {
            return "";
        }
        return "<" + list.stream().map(TypeVariable::getName).collect(Collectors.joining(", ")) + ">";
    }

    private static String printModifiers(int mod) {
        var result = "";
        if (Modifier.isPrivate(mod)) {
            result += "private ";
        } else if (Modifier.isProtected(mod)) {
            result += "protected ";
        } else if (Modifier.isPublic(mod)) {
            result += "public ";
        }
        if (Modifier.isStatic(mod)) {
            result += "static ";
        }
        return result;
    }

    private static void printClassHead(Class<?> clazz, PrintWriter writer, String tabs, String className) {
        var mod = printModifiers(clazz.getModifiers());

        var superClass = clazz.getSuperclass();

        if (clazz.isInterface()) {
            writer.print(tabs + mod + "interface " + className + printGenericType(clazz) + " ");

            var interfacesList = List.of(clazz.getInterfaces());
            if (!interfacesList.isEmpty()) {
                writer.print(" extends " + interfacesList.stream()
                        .map(i -> i.getSimpleName() + printGenericType(i))
                        .collect(Collectors.joining(", " )));
            }
        } else {
            writer.print(tabs + mod + "class " + className + printGenericType(clazz) + " ");

            if (!Object.class.getName().equals(superClass.getName())) {
                writer.print("extends " + superClass.getSimpleName() + printGenericType(superClass));
            }

            var interfacesList = List.of(clazz.getInterfaces());
            if (!interfacesList.isEmpty()) {
                writer.print(" implements " + interfacesList.stream()
                        .map(i -> i.getSimpleName() + printGenericType(i))
                        .collect(Collectors.joining(", ")));
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

    private static Set<String> getPackages(Class<?> clazz) {
        Set<String> packages = new TreeSet<>();
        packages.add(clazz.getPackageName());
        packages.add(clazz.getSuperclass().getPackageName());

        for (var field : clazz.getDeclaredFields()) {
            packages.add(field.getType().getPackageName());
        }

        for (var method : clazz.getDeclaredMethods()) {
            packages.add(method.getReturnType().getClass().getPackageName());
            for (var type : method.getParameterTypes()) {
                packages.add(type.getClass().getPackageName());
            }

            for (var exception : method.getExceptionTypes()) {
                packages.add(exception.getPackageName());
            }
        }

        for (var constructor : clazz.getDeclaredConstructors()) {
            for (var type : constructor.getParameterTypes()) {
                packages.add(type.getClass().getPackageName());
            }

            for (var exception : constructor.getExceptionTypes()) {
                packages.add(exception.getPackageName());
            }
        }

        for (var item : clazz.getInterfaces()) {
            packages.add(item.getPackageName());
        }

        for (var item : clazz.getClasses()) {
            var result = getPackages(item);
            packages.addAll(result);
        }

        return packages;
    }

    private static void printAllFromPackages(Set<String> packages, PrintWriter writer) {
        for (var item : packages) {
            writer.println("import " + item + ".*;");
        }
    }

    /**
     * Prints given class structure. Methods and constructors printed without implementation.
     * Saves all modifiers from fields, methods and classes.
     * @param someClass class to print
     */
    public static String printStructure(Class<?> someClass) throws IOException {
        String fileName = "src/test/java/com/hw/ref/" + someClass.getSimpleName() + "Printed.java";
        try (var writer = new PrintWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            writer.println("package " + someClass.getPackageName() + ";\n");
            var packages = getPackages(someClass);
            packages.remove(someClass.getPackageName());
            printAllFromPackages(packages, writer);
            writer.println();
            printClass(someClass, writer, "", someClass.getSimpleName() + "Printed");
        }
        return fileName;
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
        String result = "";
        String modifiers = Modifier.toString(method.getModifiers());
        result += modifiers + " " + method.getReturnType().getName() + " " + method.getName() + "(";
        var parameters = method.getParameterTypes();
        Arrays.sort(parameters, Comparator.comparing(Type::toString));
        for (var parameter : parameters) {
            result += parameter.getName() + " ";
        }
        result += ")";
        var exceptionList = method.getExceptionTypes();
        Arrays.sort(exceptionList);
        result += " throws " + List.of(exceptionList)
                .stream()
                .map(Class::getName)
                .collect(Collectors.joining(", "));
        return result;
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
