package com.kr.md5;

import org.junit.jupiter.api.*;

import java.io.*;
import java.security.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MDTest {

    private final MD md = new MD();

    @Test
    void nonexistingFileEqualsTest() throws IOException, NoSuchAlgorithmException {
        var file = new File("kekkrkrkrkrk");
        assertArrayEquals(md.simpleCount(file), md.forkJoinCount(file));
    }

    @Test
    void singleFileEqualsTest() throws IOException, NoSuchAlgorithmException {
        var file = new File("./src/main/resources/testFile");
        assertArrayEquals(md.simpleCount(file), md.forkJoinCount(file));
    }

    @Test
    void folderFileEqualsTest() throws IOException, NoSuchAlgorithmException {
        var file = new File("./src/main/resources");
        assertArrayEquals(md.simpleCount(file), md.forkJoinCount(file));
    }

    @Test
    void allFilesEqualsTest() throws IOException, NoSuchAlgorithmException {
        var file = new File("./src");
        assertArrayEquals(md.simpleCount(file), md.forkJoinCount(file));
    }

    @Test
    void countTwiceSimpleCountingTest() throws IOException, NoSuchAlgorithmException {
        var file = new File("./src");
        assertArrayEquals(md.simpleCount(file), md.simpleCount(file));
    }

    @Test
    void countTwiceComplexCountingTest() throws IOException, NoSuchAlgorithmException {
        var file = new File("./src");
        assertArrayEquals(md.forkJoinCount(file), md.forkJoinCount(file));
    }
}