package com.hw.hashtable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {


    @Test
    void add(){
        List lst = new List();
        lst.add("a", "b");
        assertEquals("b", lst.findValueByKey("a"));
    }

    @Test
    void findValueByKeyInEmpty() {
        List lst = new List();
        assertNull(lst.findValueByKey("a"));
    }

    @Test
    void findValueByKeyExisting() {
        List lst = new List();
        lst.add("a", "b");
        assertEquals("b", lst.findValueByKey("a"));
    }

    @Test
    void replaceExistingKey() {
        List lst = new List();
        lst.add("a", "b");
        lst.replace("a", "c");
        assertEquals("c", lst.findValueByKey("a"));
    }

    @Test
    void replaceNewKey() {
        List lst = new List();
        lst.add("a", "b");
        lst.replace("b", "c");
        assertEquals("c", lst.findValueByKey("b"));
    }


    @Test
    void deleteExisting() {
        List lst = new List();
        lst.add("a", "b");
        lst.delete("a");
        assertNull(lst.findValueByKey("a"));

    }

    @Test
    void deleteNew(){
            List lst = new List();
            lst.add("a", "b");
            lst.delete("c");
            assertEquals("b", lst.findValueByKey("a"));
    }
}