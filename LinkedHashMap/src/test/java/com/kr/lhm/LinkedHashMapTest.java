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
    void addManyTest() {
        for (int i = 1; i < 10; i++) {
            map.put(i, "aaa");
        }

        for (int i = 1; i < 10; i++) {
            assertTrue(map.containsKey(i));
        }
    }

    @Test
    void addAndRemoveSizeTest() {
        map.put(1, "aaa");
        map.remove(1);
        assertEquals(0, map.size());
    }

    @Test
    void addAndRemoveNonexistingSizeTest() {
        map.put(1, "aaa");
        map.remove(2);
        assertEquals(1, map.size());
    }

    @Test
    void simpleContainsTest() {
        assertFalse(map.containsKey(1));
    }

    @Test
    void addContainsTest() {
        map.put(1, "aaa");
        assertTrue(map.containsKey(1));
    }

    @Test
    void addTwiceContainsTest() {
        map.put(1, "aaa");
        map.put(1, "baa");
        assertTrue(map.containsKey(1));
    }

    @Test
    void addTwiceContainsValueTest() {
        map.put(1, "aaa");
        map.put(1, "baa");
        assertTrue(map.containsValue("baa"));
    }

    @Test
    void addTwiceAnotherContainsValueTest() {
        map.put(1, "aaa");
        map.put(1, "baa");
        assertTrue(map.containsValue("aaa"));
    }

    @Test
    void removeContainsValueTest() {
        map.put(1, "aaa");
        map.remove(1);
        assertFalse(map.containsKey(1));
    }

    @Test
    void removeAnotherContainsValueTest() {
        map.put(1, "aaa");
        map.remove(2);
        assertTrue(map.containsKey(1));
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

    @Test
    void forManyEachTest() {
        map.put(1, "a");
        map.put(2, "b");
        var iterator = map.entrySet().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(new MyEntry<>(1, "a"), iterator.next());
        assertEquals(new MyEntry<>(2, "b"), iterator.next());
    }


}