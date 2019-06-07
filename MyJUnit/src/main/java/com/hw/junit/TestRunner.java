package com.hw.junit;

import com.google.common.io.*;
import com.hw.junit.annotaion.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.jar.*;

/**
 * Class for runnig test using annotation from current package.
 */
public class TestRunner {

    @NotNull private final ForkJoinPool forkJoinPool;
    @NotNull private final Printer printer;
    @NotNull private String path;

    /**
     * Creates new test runner
     */
    public TestRunner() {
        forkJoinPool = new ForkJoinPool();
        printer = new Printer();
    }


    /**
     * For all .class and .jar files in directory runs all methods accordiong to annotations
     * @param rootPath root on package tree
     * @param filePath directory to look for test
     */
    public void test(@NotNull String rootPath, @NotNull String filePath) {
        path = rootPath;
        var file = new File(filePath);
        var classList = getClasses(file);
        List<ForkJoinTask<?>> list = new ArrayList<>();
        
        for (var clzz : classList) {
            list.add(forkJoinPool.submit(() -> {runTests(clzz);}));
        }

        for (var task : list) {
            task.join();
        }
        
        printer.addStatistic();
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
                var entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    var jarEntry = entries.nextElement();
                    if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
                        continue;
                    }

                    list.add(classLoader.loadClass(getClassName(new File(jarEntry.getName()))));
                }
            } catch (IOException e) {
                return List.of();
            }
            return list;
        }

        if ("class".equals(ext)) {
            try {

                var classLoader = URLClassLoader.newInstance(new URL[]{new File(path).toURI().toURL()});

                return List.of(classLoader.loadClass(getClassName(file)));
            } catch (IOException e) {
                return List.of();
            }
        }
        return List.of();
    }

    private String getClassName(File file) {
        return new File(path).toPath().relativize(file.toPath()).toString()
                .replace(File.separator, ".").replace(".class", "");
    }

    private static String getDirectory(@NotNull File fie) {
        return fie.getParent();
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
            try {
                return makeClass(file);
            } catch (ClassNotFoundException e) {
                return List.of();
            }
        }
        return List.of();
    }

    private void runIfAnnotated(@NotNull Class<?> clazz, @NotNull Class<? extends Annotation> annotation, @Nullable Object instance)
            throws IllegalAccessException, InvocationTargetException {

        for (var method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(annotation)) {
                if (method.getParameterCount() != 0) {
                    printer.failedAnnotated(method, annotation, "nonzero number of parameters");
                    continue;
                }
                if (instance == null) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        method.invoke(null);
                    } else {
                        printer.failedAnnotated(method, annotation, "nonstatic declaration");
                    }
                } else {
                    method.invoke(instance);
                }
            }
        }
    }

    private void runTest(@NotNull Class<?> clzz, @NotNull Method method) {
        try {
            var instance = clzz.getConstructor().newInstance();
            runIfAnnotated(clzz, Before.class, instance);
            var annotation = method.getAnnotation(Test.class);
            var exception = annotation.expected();

            var time = System.currentTimeMillis();
            try {
                method.setAccessible(true);
                method.invoke(instance);
                printer.addPassed(method, System.currentTimeMillis() - time);
            } catch (Exception e) {
                if (exception.isAssignableFrom(e.getCause().getClass())) {
                    printer.addPassed(method, System.currentTimeMillis() - time);
                } else {
                    printer.addFailed(method, "throws " + e + " because of " + e.getCause(),
                            System.currentTimeMillis() - time);
                }
            }
            runIfAnnotated(clzz, After.class, instance);
        } catch (Exception e) {
            printer.add("Failed to test " + method);
        }
    }

    private void runTests(@NotNull Class<?> clzz) {
        try {

            var instance = clzz.getConstructor().newInstance();
            runIfAnnotated(clzz, BeforeClass.class, null);

            var taskList = new ArrayList<ForkJoinTask<?>>();
            for (var method : clzz.getDeclaredMethods()) {
                method.setAccessible(true);
                if (method.isAnnotationPresent(Test.class)) {
                    var annotation = method.getAnnotation(Test.class);
                    if (annotation.ignore().equals(Test.EMPTY_VALUE)) {
                        taskList.add(forkJoinPool.submit(() -> runTest(clzz, method)));
                    } else {
                        printer.addIgnore(method, annotation.ignore());
                    }
                }
            }
            for (var task : taskList) {
                task.join();
            }

            runIfAnnotated(clzz, AfterClass.class, null);
        } catch (Exception e) {
            printer.add("Testing class " + clzz + " failed because of " + e);
        }
    }

    private static class Printer {
        private int counter;
        private int totalCounter;
        @NotNull private final Object object1 = "a";
        @NotNull private final Object object2 = "b";
        private final StringBuilder buffer;

        private Printer() {
            buffer = new StringBuilder(1024);
        }

        private synchronized void add(@NotNull String string) {
            buffer.append(string).append("\n");
        }

        private void incTests() {
            synchronized (object1) {
                totalCounter++;
            }
        }
        
        private void incPassed() {
            incTests();
            synchronized (object2) {
                counter++;
            }
        }
        
        private void addFailed(Object test, String message, long time) {
            add("Test " + test + " failed " + message + " ( " + time  + " ms  )");
            incTests();
        }

        private void addIgnore(Object test, String message) {
            add("Test " + test + " ignored " + message);
        }

        private void addPassed(Object test, long time) {
            add("Test " + test + " passed ( " + time + " ms )");
            incPassed();
        }

        private void failedAnnotated(Object test, Object annotaion, String message) {
            add("Cannot run " + test + " annotated with " + annotaion + " because of " + message);
        }
        
        private void addStatistic() {
            add("Statistic is " + counter + "/" + totalCounter);
        }
        
        private String get() {
            return buffer.toString();
        }
    }
}
