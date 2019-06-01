package com.hw.junit;

import com.google.common.io.Files;
import com.hw.junit.annotaion.*;
import org.jetbrains.annotations.*;
import org.testng.annotations.BeforeClass;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.*;

public class TestRunner {

    private final ForkJoinPool forkJoinPool;
    private final Printer printer;
    private volatile int counter;
    private volatile int totalCounter;

    public TestRunner() {
        forkJoinPool = new ForkJoinPool();
        printer = new Printer();
    }

    public void test(@NotNull String path) {
        var file = new File(path);
        var classList = getClasses(file);
        for (var clzz : classList) {
            runTests(clzz);
        }

        System.out.println("Statistics is " + counter + "/" + totalCounter);
        System.out.println(printer.get());
    }

    private String className(@NotNull File file) {
        return file.toPath().toString();
    }

    private List<Class<?>> makeClass(@NotNull File file) throws ClassNotFoundException {
        String ext = Files.getFileExtension(file.getName());
        if ("jar".equals(ext)) {
            var list = new ArrayList<Class<?>>();
            try (var jarFile = new JarFile(file.getPath());
                 var classLoader = URLClassLoader.newInstance(new URL[]{new URL("jar:file:" + file.getPath() + "!/")})) {
                Enumeration<JarEntry> e = jarFile.entries();
                while (e.hasMoreElements()) {
                    JarEntry je = e.nextElement();
                    if(je.isDirectory() || !je.getName().endsWith(".class")) {
                        continue;
                    }

//                    String className = je.getName().substring(0, je.getName().length() - 6);
                    String className = className(file).replace('/', '.');
                    list.add(classLoader.loadClass(className));
                }
            } catch (IOException e) {
                return List.of();
            }
            return list;
        }

        if ("class".equals(ext)) {
            try {

                var url = new File(getDirectory(file)).toURI().toURL();
                var classLoader = URLClassLoader.newInstance(new URL[]{url});

                String className = file.getName().substring(0, file.getName().length() - 6);
                String className1 = className(file).substring(0, className(file).length() - 6).replace('/', '.');

                return List.of(classLoader.loadClass("com.hw.junit.testsrc.TestOneClass"));
            } catch (IOException e) {
                return List.of();
            }
        }
        return List.of();
    }

    private static String getDirectory(File fie) {
        return fie.getParent() + "/";
    }

    private List<Class<?>> getClasses(@NotNull File file) {
        if (file.isDirectory()) {
            var list = new ArrayList<Class<?>>();
            for (var inner : file.listFiles()) {
                list.addAll(getClasses(inner));
            }
            return list;
        }

        if (file.isFile()) {
//            System.out.println(file);
            try {
                return makeClass(file);
            } catch (ClassNotFoundException e) {
                System.out.println(e);
                return List.of();
            }
        }

        return List.of();
    }

    private void runIfAnnotated(@NotNull Class<?> clazz, @NotNull Class<? extends Annotation> annotation)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        var instance = clazz.getConstructor().newInstance();
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                method.setAccessible(true);
                method.invoke(instance);
            }
        }
    }

    private void runTest(Class<?> clzz, Method method) {
        try {
            var instance = clzz.getConstructor().newInstance();
            runIfAnnotated(clzz, BeforeClass.class);
            ++totalCounter;
            var annotation = method.getAnnotation(Test.class);
            var exception = annotation.expected();

            try {
                method.invoke(instance);
                ++counter;
                printer.addPassed(method);
            } catch (Exception e) {
                if (exception.isAssignableFrom(e.getClass())) {
                    printer.addPassed(method);
                    ++counter;
                } else {
                    printer.addFailed(method, "throwed " + e );
                }
            }

            runIfAnnotated(clzz, AfterClass.class);
        } catch (Exception e) {
            printer.add("Failed to test " + method + " " + e);
        }
    }

    private void runTests(Class<?> clzz) {
        try {

            runIfAnnotated(clzz, Before.class);
            var l = clzz.getConstructor().newInstance();
            List<ForkJoinTask<?>> methods = new ArrayList<>();
            for (var method : l.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    var annotation = method.getAnnotation(Test.class);
                    if (annotation.ignore().equals(Test.EMPTY_VALUE)) {
                        methods.add(forkJoinPool.submit(() -> runTest(clzz, method)));
                    } else {
                        printer.addIgnore(method, annotation.ignore());
                    }

                }
            }
            for (var task : methods) {
                task.join();
            }

            runIfAnnotated(clzz, After.class);

        } catch (Exception e) {
            printer.add("Testing class " + clzz + " failed because of" + e);
        }

    }

    private class Printer {
        private final StringBuilder buffer;

        private Printer() {
            buffer = new StringBuilder(1024);
        }

        private synchronized void add(@NotNull String string) {
            buffer.append(string).append("\n");
        }

        private void addFailed(Object test, String message) {
            add("Test " + test + " failed " + message);
        }

        private void addIgnore(Object test, String message) {
            add("Test " + test + " ignored " + message);
        }

        private void addPassed(Object test) {
            add("Test " + test + " passed");
        }

        private String get() {
            return buffer.toString();
        }
    }

}
