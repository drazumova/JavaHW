package com.hw.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    private HashTable hashTab;

    @BeforeEach
    void init(){
        hashTab = new HashTable(10);
    }

    @Test
    void sizeEmpty() {
        assertEquals(0, hashTab.size());
    }


    @Test
    void sizeDelete(){
        hashTab.put("a", "b");
        hashTab.remove("a");
        assertEquals(hashTab.size(), 0);
    }

    @Test
    void sizeAdded() {
        hashTab.put("a", "b");
        assertEquals(1, hashTab.size());
    }

    @Test
    void sizeAddExisting() {
        hashTab.put("a", "b");
        hashTab.put("a", "c");
        assertEquals(1, hashTab.size());
    }


    @Test
    void sizeClear() {
        hashTab.put("a", "b");
        hashTab.clear();
        assertEquals(0, hashTab.size());
    }

    @Test
    void containsExisting() {
        hashTab.put("a", "b");
        assertTrue(hashTab.contains("a"));
    }

    @Test
    void containsNull() {
        assertThrows(InvalidParameterException.class, () -> {
            hashTab.put(null, "b");
        });
    }

    @Test
    void containsDefunct() {
        hashTab.put("a", "b");
        assertFalse(hashTab.contains("c"));
    }

    @Test
    void getExisting() {
        hashTab.put("a", "b");
        assertEquals("b", hashTab.get("a"));
    }

    @Test
    void getNull(){
        assertThrows(InvalidParameterException.class, () -> {
            hashTab.get(null);
        });
    }

    @Test
    void getDefunct() {
        hashTab.put("a", "b");
        assertNull(hashTab.get("c"));
    }

    @Test
    void putNew() {
        hashTab.put("a", "b");
        assertEquals("b", hashTab.get("a"));
    }

    @Test
    void putNull() {
        assertThrows(InvalidParameterException.class, () -> {
            hashTab.put(null, "b");
        });
    }

    @Test
    void putNewReturn() {
        assertNull(hashTab.put("a", "b"));
    }

    @Test
    void putExisting() {
        hashTab.put("a", "b");
        hashTab.put("a", "c");
        assertEquals("c", hashTab.get("a"));
    }

    @Test
    void putExistingReturn() {
        hashTab.put("a", "b");
        assertEquals("b", hashTab.put("a", "c"));
    }


    @Test
    void putWithResize() {
        hashTab.put("a", "b");
        hashTab.put("b", "c");
        hashTab.put("c", "d");
        hashTab.put("1", "2");
        hashTab.put("2", "3");

        boolean c1 = hashTab.get("a").equals("b");
        boolean c2 = hashTab.get("b").equals("c");
        boolean c3 = hashTab.get("c").equals("d");
        boolean c4 = hashTab.get("1").equals("2");
        boolean c5 = hashTab.get("2").equals("3");

        assertTrue(c1 && c2 && c3 && c4 && c5);
    }

    @Test
    void removeDefunct() {
        hashTab.put("a", "b");
        hashTab.remove("c");
        assertEquals("b", hashTab.get("a"));
    }

    @Test
    void removaNull() {
        assertThrows(InvalidParameterException.class, () -> {
            hashTab.remove(null);
        });
    }

    @Test
    void removeDefunctReturn() {
        hashTab.put("a", "b");
        assertNull(hashTab.remove("c"));

    }

    @Test
    void removeExisting() {
        hashTab.put("a", "b");
        hashTab.remove("a");
        assertNull( hashTab.get("a"));
    }

    @Test
    void removeExistingReturn() {
        hashTab.put("a", "b");
        assertEquals("b", hashTab.remove("a"));
    }

    @Test
    void clear() {
        hashTab.put("a", "b");
        hashTab.put("c", "d");
        hashTab.clear();
        assertEquals(0, hashTab.size());
    }

}