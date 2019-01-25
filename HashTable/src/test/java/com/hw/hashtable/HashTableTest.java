package com.hw.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    private HashTable hashTable;

    @BeforeEach
    void init() {
        hashTable = new HashTable(10);
    }

    @Test
    void sizeEmpty() {
        assertEquals(0, hashTable.size());
    }


    @Test
    void sizeDelete() {
        hashTable.put("a", "b");
        hashTable.remove("a");
        assertEquals(hashTable.size(), 0);
    }

    @Test
    void sizeAdded() {
        hashTable.put("a", "b");
        assertEquals(1, hashTable.size());
    }

    @Test
    void sizeAddExisting() {
        hashTable.put("a", "b");
        hashTable.put("a", "c");
        assertEquals(1, hashTable.size());
    }


    @Test
    void sizeClear() {
        hashTable.put("a", "b");
        hashTable.clear();
        assertEquals(0, hashTable.size());
    }

    @Test
    void containsExisting() {
        hashTable.put("a", "b");
        assertTrue(hashTable.contains("a"));
    }

    @Test
    void containsNull() {
        assertThrows(InvalidParameterException.class, () -> {
            hashTable.put(null, "b");
        });
    }

    @Test
    void containsDefunct() {
        hashTable.put("a", "b");
        assertFalse(hashTable.contains("c"));
    }

    @Test
    void getExisting() {
        hashTable.put("a", "b");
        assertEquals("b", hashTable.get("a"));
    }

    @Test
    void getNull(){
        assertThrows(InvalidParameterException.class, () -> hashTable.get(null));
    }

    @Test
    void getDefunct() {
        hashTable.put("a", "b");
        assertNull(hashTable.get("c"));
    }

    @Test
    void putNew() {
        hashTable.put("a", "b");
        assertEquals("b", hashTable.get("a"));
    }

    @Test
    void putNull() {
        assertThrows(InvalidParameterException.class, () -> hashTable.put(null, "b"));
    }

    @Test
    void putNewReturn() {
        assertNull(hashTable.put("a", "b"));
    }

    @Test
    void putExisting() {
        hashTable.put("a", "b");
        hashTable.put("a", "c");
        assertEquals("c", hashTable.get("a"));
    }

    @Test
    void putExistingReturn() {
        hashTable.put("a", "b");
        assertEquals("b", hashTable.put("a", "c"));
    }


    @Test
    void putWithResize() {
        hashTable.put("a", "b");
        hashTable.put("b", "c");
        hashTable.put("c", "d");
        hashTable.put("1", "2");
        hashTable.put("2", "3");

        boolean c1 = hashTable.get("a").equals("b");
        boolean c2 = hashTable.get("b").equals("c");
        boolean c3 = hashTable.get("c").equals("d");
        boolean c4 = hashTable.get("1").equals("2");
        boolean c5 = hashTable.get("2").equals("3");

        assertTrue(c1 && c2 && c3 && c4 && c5);
    }

    @Test
    void removeDefunct() {
        hashTable.put("a", "b");
        hashTable.remove("c");
        assertEquals("b", hashTable.get("a"));
    }

    @Test
    void removaNull() {
        assertThrows(InvalidParameterException.class, () -> hashTable.remove(null));
    }

    @Test
    void removeDefunctReturn() {
        hashTable.put("a", "b");
        assertNull(hashTable.remove("c"));

    }

    @Test
    void removeExisting() {
        hashTable.put("a", "b");
        hashTable.remove("a");
        assertNull( hashTable.get("a"));
    }

    @Test
    void removeExistingReturn() {
        hashTable.put("a", "b");
        assertEquals("b", hashTable.remove("a"));
    }

    @Test
    void clear() {
        hashTable.put("a", "b");
        hashTable.put("c", "d");
        hashTable.clear();
        assertEquals(0, hashTable.size());
    }

}