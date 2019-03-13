package com.kr.lhm;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashMapTest {

    private LinkedHashMap<Integer, String> map;

    @BeforeEach
    void init() {
        map = new LinkedHashMap<>(1);
    }

    @Test
    void simpleSizeTest() {
        assertEquals(0, map.size());
    }

    @Test
    void addSizeTest() {
        map.put(1, "aaa");
        assertEquals(1, map.size());
    }

    @Test
    void addAndRemoveSizeTest() {
        map.put(1, "aaa");
        map.remove(1);
        assertEquals(0, map.size());
    }

    @Test
    void simpleIsEmptyTest() {
        assertTrue(map.isEmpty());
    }

    @Test
    void addIsEmptyTest() {
        map.put(1, "a");
        assertFalse(map.isEmpty());
    }

    @Test
    void forEachTest() {
        map.put(1, "a");
        map.put(2, "b");
        var iterator = map.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new MyEntry<>(1, "a"), iterator.next());
        assertEquals(new MyEntry<>(2, "b"), iterator.next());
    }
}