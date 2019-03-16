package com.hw.qsort;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class Main {
    
    private static <T extends Comparable<? super T>> void sortAndPrint(T[] arrayForSimple, T[] arrayForSmartCached,
                                         T[] arrayForSmartFixed, PrintWriter writer, int count) {
        
        var sorterFixedPool = new QSorter(Runtime.getRuntime().availableProcessors());
        var sorterCachedPool = new QSorter(null);

        var timeBefore = System.nanoTime();
        sorterCachedPool.simpleQSort(arrayForSimple);
        Map<Long, String> times = new TreeMap<>();
        times.put(System.nanoTime() - timeBefore, "Simple");

        try {
            timeBefore = System.nanoTime();
            sorterCachedPool.smartQSort(arrayForSmartCached);
            times.put(System.nanoTime() - timeBefore, "Cached thread pool");

            timeBefore = System.nanoTime();
            sorterFixedPool.smartQSort(arrayForSmartFixed);
            times.put(System.nanoTime() - timeBefore, "Fixed thread pool");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        writer.println("For " + count + " numbers speed order is: ");
        final double convert = 1.0e-6;
        for (var item : times.entrySet()) {
            writer.printf("%s (%.3f miliseconds) ", item.getValue(), item.getKey() * convert);
        }
        writer.println("\n");
    }
    
    public static void main(String... args) {
        try (var writer = new PrintWriter(new FileOutputStream("mainResult"),
                true, StandardCharsets.UTF_8)) {
            for (int count = 1; count < 10000000; count *= 10) {
                var arrayForSimple = new Integer[count];
                var arrayForSmartCached = new Integer[count];
                var arrayForSmartFixed = new Integer[count];
                
                var random = new Random(count);

                for (int j = 0; j < count; j++) {
                    int nextValue = random.nextInt();
                    arrayForSimple[j] = nextValue;
                    arrayForSmartCached[j] = nextValue;
                    arrayForSmartFixed[j] = nextValue;
                }

                sortAndPrint(arrayForSimple, arrayForSmartCached, arrayForSmartFixed, writer, count);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
