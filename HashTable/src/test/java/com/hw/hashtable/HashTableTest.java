package com.hw.hashtable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void sizeEmpty() {
        HashTable ht = new HashTable(10);
        assertEquals(0, ht.size());
    }

    @Test
    void sizeAdded() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertEquals(1, ht.size());
    }

    @Test
    void sizeAddExisting() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        ht.put("a", "c");
        assertEquals(1, ht.size());
    }

    @Test
    void sizeClear() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        ht.clear();
        assertEquals(0, ht.size());
    }

    @Test
    void containsExisting() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertTrue(ht.contains("a"));
    }

    @Test
    void containsDefunct() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertFalse(ht.contains("c"));
    }

    @Test
    void getExisting() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertEquals("b", ht.get("a"));
    }

    @Test
    void getDefunct() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertNull(ht.get("c"));
    }

    @Test
    void putNew() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertEquals("b", ht.get("a"));
    }

    @Test
    void putNewReturn() {
        HashTable ht = new HashTable(10);
        assertNull(ht.put("a", "b"));
    }

    @Test
    void putExisting() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        ht.put("a", "c");
        assertEquals("c", ht.get("a"));
    }

    @Test
    void putExistingReturn() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertEquals("b", ht.put("a", "c"));
    }

    @Test
    void removeDefunct() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        ht.remove("c");
        assertEquals("b", ht.get("a"));
    }

    @Test
    void removeDefunctReturn() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertNull(ht.remove("c"));

    }

    @Test
    void removeExisting() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        ht.remove("a");
        assertNull( ht.get("a"));
    }

    @Test
    void removeExistingReturn() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        assertEquals("b", ht.remove("a"));
    }

    @Test
    void clear() {
        HashTable ht = new HashTable(10);
        ht.put("a", "b");
        ht.put("c", "d");
        ht.clear();
        assertEquals(0, ht.size());
    }

}