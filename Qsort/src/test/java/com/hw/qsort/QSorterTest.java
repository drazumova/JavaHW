package com.hw.qsort;

import org.junit.jupiter.api.*;

import java.util.*;

import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.*;

class QSorterTest {

    private final QSorter sorter = new QSorter(null);

    @Test
    void reverseSimpleQSortTest() {
        var list = new Integer[100];
        for (int i = 0; i < 100; i++) {
            list[i] = 100 - i;
        }

        sorter.simpleQSort(list);
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(i + 1), list[i]);
        }
    }

    @Test
    void randomSimpleQSortTest() {
        Random random = new Random(10);
        int n = 10000;
        var list = new Integer[n];
        var correctList = new Integer[n];
        for (int i = 0; i < n; i++) {
            list[i] = correctList[i] = random.nextInt(10);
        }

        sort(correctList);
        sorter.simpleQSort(list);

        assertTrue(Arrays.equals(correctList, list));
    }

    @Test
    void reverseSmartQSortTest() throws InterruptedException {
        var list = new Integer[100];
        for (int i = 0; i < 100; i++) {
            list[i] = 100 - i;
        }

        sorter.smartQSort(list);
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(i + 1), list[i]);
        }
    }

    @Test
    void randomSmartQSortTest() throws InterruptedException {
        Random random = new Random(10);
        int n = 10000;
        var list = new Integer[n];
        var correctList = new Integer[n];
        for (int i = 0; i < n; i++) {
            list[i] = correctList[i] = random.nextInt();
        }

        sort(correctList);
        sorter.smartQSort(list);
        assertTrue(Arrays.equals(correctList, list));
    }
}