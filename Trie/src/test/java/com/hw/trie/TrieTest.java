package com.hw.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TrieTest {

    private Trie trie;

    @BeforeEach
    void init() {
        trie = new Trie();
    }

    @Test
    void serializeAndDeserialize() throws IOException {
        File kek = new File("kek.txt");

        trie.add("a");
        trie.add("b");
        trie.add("a");
        try (var fileOutStream = new FileOutputStream(kek)) {
            trie.serialize(fileOutStream);
        }

        try (var fileInStream = new FileInputStream(kek)) {
            trie.deserialize(fileInStream);
        }

        kek.delete();

        assertTrue(trie.contains("a"));
        assertTrue(trie.contains("b"));
        assertEquals(2, trie.howManyStartsWithPrefix("a"));
    }

    @Test
    void serializeAndDeserializeEmpty() throws IOException {
        File kek = new File("kek.txt");

        try (var fileOutStream = new FileOutputStream(kek)) {
            trie.serialize(fileOutStream);
        }

        try (var fileInStream = new FileInputStream(kek)) {
            trie.deserialize(fileInStream);
        }

        kek.delete();
        assertEquals(0, trie.size());
    }

    @Test
    void addNewString() {
        trie.add("aaa");
        assertTrue(trie.contains("aaa"));
    }

    @Test
    void addNewStringReturnCheck() {
        assertTrue(trie.add("aaa"));
    }

    @Test
    void addExistingString() {
        trie.add("aaa");
        trie.add("aaa");
        assertEquals(2, trie.howManyStartsWithPrefix("aaa"));
    }

    @Test
    void addExistingStringReturnCheck() {
        trie.add("aaa");
        assertFalse(trie.add("aaa"));
    }

    @Test
    void containsTrue() {
        trie.add("aaa");
        trie.add("aaaa");
        assertTrue(trie.contains("aaa"));
    }

    @Test
    void containsFalse() {
        trie.add("aaa");
        trie.add("aaaa");
        assertFalse(trie.contains("aab"));
    }

    @Test
    void removeNonexisting() {
        assertFalse(trie.remove("a"));
    }

    @Test
    void removeExisting() {
        trie.add("a");
        trie.remove("a");
        assertFalse(trie.contains("a"));
    }

    @Test
    void removeExistingCopy() {
        trie.add("a");
        trie.add("a");
        trie.remove("a");
        assertTrue(trie.contains("a"));
    }

    @Test
    void removeExistingReturnCheck() {
        trie.add("a");
        assertTrue(trie.remove("a"));
    }

    @Test
    void removeNonexistingReturnCheck() {
        assertFalse(trie.remove("a"));
    }

    @Test
    void sizeAfterAdd() {
        trie.add("a");
        trie.add("b");
        assertEquals(2, trie.size());
    }

    @Test
    void sizeAfterRemove() {
        trie.add("a");
        trie.add("b");
        trie.remove("a");
        assertEquals(1, trie.size());
    }

    @Test
    void sizeAfterRemoveNonexisting() {
        trie.add("a");
        trie.add("b");
        trie.remove("c");
        assertEquals(2, trie.size());
    }

    @Test
    void addAfterRemove() {
        trie.add("a");
        trie.remove("a");
        trie.add("a");
        assertEquals(1, trie.howManyStartsWithPrefix("a"));
    }

    @Test
    void howManyStartsWithPrefixAfterAdd() {
        trie.add("a");
        trie.add("aaa");
        trie.add("a");
        assertEquals(3, trie.howManyStartsWithPrefix("a"));
    }

    @Test
    void howManyStartsWithPrefixAfterRemove() {
        trie.add("a");
        trie.add("aaa");
        trie.add("a");
        trie.remove("a");
        assertEquals(2, trie.howManyStartsWithPrefix("a"));
    }

    @Test
    void howManyStartsWithPrefixNonExisting() {
        assertEquals(0, trie.howManyStartsWithPrefix("aaa"));
    }
}