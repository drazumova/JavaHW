package com.hw.junit;

import org.junit.jupiter.api.*;

import javax.tools.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TestRunnerTest {


    private TestRunner testRunner;

    @BeforeEach
    void init() {
        testRunner = new TestRunner();
    }

    @Test
    void simpleOneClassTest() throws IOException {
        var file = new File("src/test/java/com/hw/junit/testsrc/TestOneClass.java");
        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, file.getPath());

        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {

            var old = System.out;
            System.setOut(ps);
            testRunner.test("src/test/java/com/hw/junit/testsrc/TestOneClass.class");
            System.out.flush();
            System.setOut(old);
            var message = stream.toString();
            assertTrue(message.contains("1/1"));

        }
    }

    @Test
    void simpleInnoreTest() throws IOException {
        var file = new File("src/test/java/com/hw/junit/testsrc/TestIgnored.java");
        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, file.getPath());

        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {

            var old = System.out;
            System.setOut(ps);
            testRunner.test("src/test/java/com/hw/junit/testsrc/TestIgnored.class");
            System.out.flush();
            System.setOut(old);
            var message = stream.toString();
            assertTrue(message.contains("0/0"));
            assertTrue(message.contains("no reason"));

        }
    }

    @Test
    void simpleExpectedTest() throws IOException {
        var file = new File("src/test/java/com/hw/junit/testsrc/TestExpected.java");
        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, file.getPath());

        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {

            var old = System.out;
            System.setOut(ps);
            testRunner.test("src/test/java/com/hw/junit/testsrc/TestExpected.class");
            System.out.flush();
            System.setOut(old);
            var message = stream.toString();
            assertTrue(message.contains("1/1"));
        }
    }

    @Test
    void simpleFailedTest() throws IOException {
        var file = new File("src/test/java/com/hw/junit/testsrc/TestFailed.java");
        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, file.getPath());

        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {

            var old = System.out;
            System.setOut(ps);
            testRunner.test("src/test/java/com/hw/junit/testsrc/TestFailed.class");
            System.out.flush();
            System.setOut(old);
            var message = stream.toString();
            assertTrue(message.contains("0/1"));
        }
    }

    @Test
    void OneClassWithBeforeAndAfterTest() throws IOException {
        var file = new File("src/test/java/com/hw/junit/testsrc/TestBeforeAfter.java");
        var compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, file.getPath());

        try (var stream = new ByteArrayOutputStream();
             var ps = new PrintStream(stream)) {

            var old = System.out;
            System.setOut(ps);
            testRunner.test("src/test/java/com/hw/junit/testsrc/TestBeforeAfter.class");
            System.out.flush();
            System.setOut(old);
            var message = stream.toString();
            assertTrue(message.contains("1/1"));

        }
        System.out.println("passed");
    }

    @Test
    void OneClassWithBeforeEachTest() {

    }

    @Test
    void OneClassWithAfteEachTest() {

    }

    @Test
    void TwoClassesTest() {

    }

    @Test
    void OneJarTest() {

    }

    @Test
    void RecutsiveAllTest() {

    }
}