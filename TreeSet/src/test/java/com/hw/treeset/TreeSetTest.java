package com.hw.treeset;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;


class TreeSetTest {

    private TreeSet<Integer> treeSet;

    @BeforeEach
    private void init() {
        treeSet = new TreeSet<>();
    }

    @Test
    void testIterator() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        var iterator = treeSet.iterator();
        var list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    void testIteratorThrow() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        var iterator = treeSet.iterator();
        treeSet.remove(1);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testIteratorNotModified() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        var iterator = treeSet.iterator();
        treeSet.remove(1000);
        assertEquals(Integer.valueOf(0), iterator.next());
    }


    private void findsInit() {
        treeSet.add(1);
        treeSet.add(2);
    }

    @Test
    void testRemoveExisting() {
        treeSet.add(1);
        assertTrue(treeSet.remove(1));
    }

    @Test
    void testRemoveNonxisting() {
        assertFalse(treeSet.remove(1));
    }

    @Test
    void testSimpleAdd() {
        treeSet.add(1);
        assertEquals(Integer.valueOf(1), treeSet.first());
    }

    @Test
    void testExistingAdd() {
        treeSet.add(1);
        treeSet.add(1);
        assertEquals(1, treeSet.size());
    }

    @Test
    void testSizeEmpty() {
        assertEquals(0, treeSet.size());
    }

    @Test
    void testSizeAfterAdd() {
        treeSet.add(1);
        treeSet.add(2);
        assertEquals(2, treeSet.size());
    }

    @Test
    void testSizeAfterRemoveExisting() {
        treeSet.add(1);
        treeSet.remove(1);
        assertEquals(0, treeSet.size());
    }

    @Test
    void testSizeAfterRemoveNonexisting() {
        treeSet.add(1);
        treeSet.remove(2);
        assertEquals(1, treeSet.size());
    }

    @Test
    void testDescendingIterator() {
        treeSet.add(1);
        treeSet.add(-1);
        treeSet.add(10);
        treeSet.add(-100);

        var list1 = treeSet.toArray();
        var list2 = new ArrayList<>();
        var iterator = treeSet.descendingIterator();
        while (iterator.hasNext()) {
            list2.add(iterator.next());
        }

        for (int i = 0; i < 4; i++) {
            assertEquals(list1[i], list2.get(3 - i));
        }
    }

    @Test
    void testDescendingIteratorThrow() {
        for (int i = 0; i < 10; i++) {
            treeSet.add(i);
        }
        var iterator = treeSet.descendingIterator();
        treeSet.remove(1);
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    void testDescendingSetByIterator() {
        findsInit();
        var descendingTreeSet = treeSet.descendingSet();
        var trueIterator = treeSet.iterator();
        var reversedIterator = descendingTreeSet.descendingIterator();
        while (trueIterator.hasNext()) {
            assertEquals(trueIterator.next(), reversedIterator.next());
        }
    }

    @Test
    void testDescendingSetFirstAndLast() {
        findsInit();
        treeSet.add(222);
        treeSet.add(101);
        var descendingTreeSet = treeSet.descendingSet();
        assertEquals(treeSet.last(), descendingTreeSet.first());
        assertEquals(treeSet.first(), descendingTreeSet.last());
    }

    @Test
    void testDescendingSetLower() {
        findsInit();
        var descendingTreeSet = treeSet.descendingSet();
        assertEquals(Integer.valueOf(2), descendingTreeSet.lower(1));
    }

    @Test
    void testDescendingSetFloor() {
        findsInit();
        var descendingTreeSet = treeSet.descendingSet();
        assertEquals(Integer.valueOf(1), descendingTreeSet.floor(1));
    }

    @Test
    void testDescendingSetCeiling() {
        findsInit();
        var descendingTreeSet = treeSet.descendingSet();
        assertEquals(Integer.valueOf(2), descendingTreeSet.ceiling(2));
    }

    @Test
    void testDescendingSetHigher() {
        findsInit();
        var descendingTreeSet = treeSet.descendingSet();
        assertEquals(Integer.valueOf(1), descendingTreeSet.higher(2));
    }

    @Test
    void testFirstWithoutChanges() {
        treeSet.add(1);
        treeSet.add(100);
        assertEquals(Integer.valueOf(1), treeSet.first());
    }

    @Test
    void testFirstAfterChanges() {
        treeSet.add(1);
        treeSet.add(-1);
        assertEquals(Integer.valueOf(-1), treeSet.first());
    }

    @Test
    void testLastWithoutChanges() {
        treeSet.add(1);
        treeSet.add(-100);
        assertEquals(Integer.valueOf(1), treeSet.last());
    }

    @Test
    void testLastAfterChanges() {
        treeSet.add(1);
        treeSet.add(100);
        assertEquals(Integer.valueOf(100), treeSet.last());
    }

    @Test
    void testLower() {
        findsInit();
        assertEquals(Integer.valueOf(1), treeSet.lower(2));
    }

    @Test
    void testLowerNull() {
        findsInit();
        assertNull(treeSet.lower(1));
    }

    @Test
    void testFloor() {
        findsInit();
        assertEquals(Integer.valueOf(2), treeSet.floor(2));
    }

    @Test
    void testFloorNull() {
        findsInit();
        assertNull(treeSet.floor(0));
    }

    @Test
    void testCeiling() {
        findsInit();
        assertEquals(Integer.valueOf(1), treeSet.ceiling(1));
    }

    @Test
    void testCeilingNull() {
        findsInit();
        assertNull(treeSet.ceiling(3));
    }

    @Test
    void testHigher() {
        findsInit();
        assertEquals(Integer.valueOf(2), treeSet.higher(1));
    }

    @Test
    void testHigherNull() {
        findsInit();
        assertNull(treeSet.higher(3));
    }

    @Test
    void testContainsFalse() {
        findsInit();
        assertFalse(treeSet.contains(100));
    }

    @Test
    void testContainsTrue() {
        findsInit();
        assertTrue(treeSet.contains(1));
    }
}