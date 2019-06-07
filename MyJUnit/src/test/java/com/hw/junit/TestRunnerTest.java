package com.hw.junit;

import org.junit.jupiter.api.*;

import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.util.jar.*;
import java.util.zip.*;

import static org.junit.jupiter.api.Assertions.*;

class TestRunnerTest {

    private TestRunner testRunner;
    private static final String TEST_PATH = "aga/testsrc/";
    @BeforeEach
    void init() {
        testRunner = new TestRunner();
    }

    @AfterEach
    void clearAll() {
        clear(TEST_PATH);
    }

    void clear(String path) {
        var dir = new File(path);
        for (var file : dir.listFiles()) {
            if (file.isDirectory()) {
                clear(file.getPath());
            }

            if (file.getName().contains(".class") || file.getName().contains(".jar")) {
                file.delete();
            }
        }
    }

    @Test
    void simpleOneClassTest() throws IOException {
        var message = testOneFile("TestOneClass");
        assertTrue(message.contains("1/1"));
    }

    @Test
    void simpleInnoreTest() throws IOException {
        var message = testOneFile("TestIgnore");
        assertTrue(message.contains("0/0"));
        assertTrue(message.contains("no reason"));
    }

    @Test
    void simpleExpectedTest() throws IOException {
        var message = testOneFile("TestExpected");
        assertTrue(message.contains("1/1"));
    }

    @Test
    void simpleFailedTest() throws IOException {
        var message = testOneFile("TestFailed");
        assertTrue(message.contains("0/1"));
    }

    @Test
    void OneClassWithBeforeAndAfterTest() throws IOException {
        var message = testOneFile("TestBeforeAfter");
        assertTrue(message.contains("1/1"));
    }

    @Test
    void OneClassWithBeforeAndAfterClassTest() throws IOException {
        var message = testOneFile("TestBeforeAfterClass");
        assertTrue(message.contains("1/1"));
    }

    @Test
    void OneClassWithBeforeEachTest() throws IOException {
        var message = testOneFile("nested/TestBeforeEach");
        assertTrue(message.contains("7/7"));
    }

    @Test
    void NonstaticTest() throws IOException {
        var message = testOneFile("TestNonstatic");
        assertTrue(message.contains("1/1"));
        assertTrue(message.contains("because of nonstatic declaration"));
    }

    @Test
    void ArgsTest() throws IOException {
        var message = testOneFile("TestArgs");
        assertTrue(message.contains("1/1"));
        assertTrue(message.contains("nonzero number of parameters"));
    }

    @Test
    void ArgsStaticTest() throws IOException {
        var message = testOneFile("TestArgsStatic");
        assertTrue(message.contains("1/1"));
        assertTrue(message.contains("nonzero number of parameters"));
    }

    @Test
    void OneClassWithAfteEachTest() throws IOException {
        var message = testOneFile("nested/TestAfterEach");
         assertTrue(message.contains("5/5"));
         for (int i = 0; i < 5; i++) {
             assertTrue(message.contains("" + i));
         }
    }

    @Test
    void OneClassAfterTest() throws IOException {
        var result = testOneFile("nested/TestAfter");
        assertTrue(result.contains("1/1"));
        assertTrue(result.contains("KEK"));
    }

    @Test
    void OneClassBeforeTest() throws IOException {
        var result = testOneFile("nested/TestBefore");
        assertTrue(result.contains("1/1"));
    }

    @Test
    void AllInDirectoryTest() throws IOException {
        var compiler = ToolProvider.getSystemJavaCompiler();
        for (var file : new File(TEST_PATH).listFiles()) {
            if (file.getName().contains(".java") ) {
                compiler.run(null, null, null, file.getPath());
            }
        }
        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {

            var old = System.out;
            System.setOut(ps);
            testRunner.test("", TEST_PATH);
            System.out.flush();
            System.setOut(old);
            var message = stream.toString();
            assertTrue(message.contains("7/8"));
            assertTrue(message.contains("nonstatic declaration"));
            assertTrue(message.contains("nonzero number of parameters"));
            assertTrue(message.contains("no reason"));
        }
    }

    @Test
    void OneJarTest() throws IOException {
        var compiler = ToolProvider.getSystemJavaCompiler();
        var failed = TEST_PATH + "TestFailed";
        var passed = TEST_PATH + "TestOneClass";
        var fileFailed = new File(failed + ".java");
        var filePassed = new File(passed + ".java");
        compiler.run(null, null, null, fileFailed.getPath());
        compiler.run(null, null, null, filePassed.getPath());

        try (var fout = new FileOutputStream(TEST_PATH  + "foo.jar");
                var out = new JarOutputStream(fout)) {

            out.putNextEntry(new ZipEntry(TEST_PATH));
            out.putNextEntry(new ZipEntry(failed + ".class"));
            out.write(Files.readAllBytes(Paths.get(failed + ".class")));
            out.closeEntry();
            out.putNextEntry(new ZipEntry(passed + ".class"));
            out.write(Files.readAllBytes(Paths.get(passed + ".class")));
            out.closeEntry();
        }

        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {

            var old = System.out;
            System.setOut(ps);
            testRunner.test("", TEST_PATH  + "foo.jar");
            System.out.flush();
            System.setOut(old);
            var message = stream.toString();
            assertTrue(message.contains("1/2"));
        }
    }

    String testOneFile(String name) throws IOException {
        var file = new File(TEST_PATH + name + ".java");
        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, file.getPath());

        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {
            var old = System.out;
            System.setOut(ps);
            testRunner.test("", TEST_PATH + name + ".class");
            System.out.flush();
            System.setOut(old);

            return stream.toString();
        }
    }
}
