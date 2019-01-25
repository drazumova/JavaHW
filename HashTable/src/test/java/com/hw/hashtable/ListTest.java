package com.hw.hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    private List lst;

    @BeforeEach
    void init(){
        lst = new List();
    }

    @Test
    void add(){
        lst.add("a", "b");
        assertEquals("b", lst.findValueByKey("a"));
    }

    @Test
    void addNull(){
        assertThrows(InvalidParameterException.class, () -> {
            lst.add(null, "b");
        });

    }

    @Test
    void getHeadKeyTestNull(){
        assertNull(lst.getHeadKey());
    }

    @Test
    void getHeadKeyTest(){
        lst.add("a", "b");
        assertEquals(lst.getHeadKey(), "a");
    }

    @Test
    void getHeadValueTestNull(){
        assertNull(lst.getHeadValue());
    }

    @Test
    void getHeadValueTest(){
        lst.add("a", "b");
        assertEquals(lst.getHeadValue(), "b");
    }

    @Test
    void findValueByKeyInEmpty() {
        assertNull(lst.findValueByKey("a"));
    }

    @Test
    void findValueByKeyExisting() {
        lst.add("a", "b");
        assertEquals("b", lst.findValueByKey("a"));
    }

    @Test
    void findValueByKeyNull() {
        assertThrows(InvalidParameterException.class, () -> {
            lst.findValueByKey(null);
        });
    }

    @Test
    void replaceExistingKey() {
        lst.add("a", "b");
        lst.replace("a", "c");
        assertEquals("c", lst.findValueByKey("a"));
    }

    @Test
    void replaceNewKey() {
        lst.add("a", "b");
        lst.replace("b", "c");
        assertEquals("c", lst.findValueByKey("b"));
    }

    @Test
    void replaceNull() {
        assertThrows(InvalidParameterException.class, () -> {
            lst.replace("a", null);
        });
    }


    @Test
    void deleteExisting() {
        lst.add("a", "b");
        lst.delete("a");
        assertNull(lst.findValueByKey("a"));

    }

    @Test
    void deleteNew(){
        lst.add("a", "b");
        lst.delete("c");
        assertEquals("b", lst.findValueByKey("a"));
    }

    @Test
    void deleteNull(){
        assertThrows(InvalidParameterException.class, () -> {
            lst.delete(null);
        });
    }
}