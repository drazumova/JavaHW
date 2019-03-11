package com.hw.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    private List list;

    @BeforeEach
    void init() {
        list = new List();
    }

    @Test
    void add() {
        list.add("a", "b");
        assertEquals("b", list.findValueByKey("a"));
    }

    @Test
    void addNull() {
        assertThrows(IllegalArgumentException.class, () -> list.add(null, "b"));

    }

    @Test
    void getHeadKeyTestNull() {
        assertNull(list.getHeadKey());
    }

    @Test
    void getHeadKeyTest() {
        list.add("a", "b");
        assertEquals(list.getHeadKey(), "a");
    }

    @Test
    void getHeadValueTestNull() {
        assertNull(list.getHeadValue());
    }

    @Test
    void getHeadValueTest() {
        list.add("a", "b");
        assertEquals(list.getHeadValue(), "b");
    }

    @Test
    void findValueByKeyInEmpty() {
        assertNull(list.findValueByKey("a"));
    }

    @Test
    void findValueByKeyExisting() {
        list.add("a", "b");
        assertEquals("b", list.findValueByKey("a"));
    }

    @Test
    void findValueByKeyNull() {
        assertThrows(IllegalArgumentException.class, () -> list.findValueByKey(null));
    }

    @Test
    void replaceExistingKey() {
        list.add("a", "b");
        list.replace("a", "c");
        assertEquals("c", list.findValueByKey("a"));
    }

    @Test
    void replaceNewKey() {
        list.add("a", "b");
        list.replace("b", "c");
        assertEquals("c", list.findValueByKey("b"));
    }

    @Test
    void replaceNull() {
        assertThrows(IllegalArgumentException.class, () -> list.replace("a", null));
    }


    @Test
    void deleteExisting() {
        list.add("a", "b");
        list.delete("a");
        assertNull(list.findValueByKey("a"));

    }

    @Test
    void deleteNew() {
        list.add("a", "b");
        list.delete("c");
        assertEquals("b", list.findValueByKey("a"));
    }

    @Test
    void deleteNull() {
        assertThrows(IllegalArgumentException.class, () -> list.delete(null));
    }
}