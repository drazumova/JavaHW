package com.hw.qsort;

import org.jetbrains.annotations.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Class with mutlithread and singlethread qsort methods.
 */
public class QSorter {
    private CountDownLatch countDownLatch;
    private final ExecutorService executorService;

    private class QSortRunner<T extends Comparable<? super T>> implements Runnable {
        private final T[] array;
        private int begin;
        private final int end;

        private QSortRunner(T[] array, int begin, int end) {
            this.begin = begin;
            this.end = end;
            this.array = array;
        }

        @Override
        public void run() {
            var partitionResult = partition(array, begin, end);
            int first = partitionResult.first;
            int second = partitionResult.second;

            if (second > begin) {
                executorService.submit(new QSortRunner<>(array, begin, second));
            } else if (second == begin) {
                countDownLatch.countDown();
            }

            if (first < end) {
                begin = first;
                run();
            } else if (first == end) {
                countDownLatch.countDown();
            }

            if (first - second == 2) {
                countDownLatch.countDown();
            }

        }
    }

    private static class PartitionResult {
        private final int first;
        private final int second;

        private PartitionResult(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    /**
     * Creates new QSorter
     * @param poolSize size of thread pool, if it is null CachedThreadPool will be created
     */
    public QSorter(@Nullable Integer poolSize) {
        if (poolSize == null) {
            executorService = Executors.newCachedThreadPool();
        } else {
            executorService = Executors.newFixedThreadPool(poolSize);
        }
    }

    private static <T> void swap(T[] array, int i, int j) {
        T tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private static <T extends Comparable<? super T>> PartitionResult partition(T[] array, int begin, int end) {
        int first = begin;
        int second = end;
        T middle = array[(end + begin) / 2];

        while (first <= second) {
            while (array[first].compareTo(middle) < 0) {
                first++;
            }
            while (array[second].compareTo(middle) > 0) {
                second--;
            }
            if (first <= second) {
                swap(array, first, second);
                first++;
                second--;
            }
        }

        return new PartitionResult(first, second);
    }

    private <T extends Comparable<? super T>> void simpleQSort(T[] array, int begin, int end) {
        var partitionResult = partition(array, begin, end);
        int first = partitionResult.first;
        int second = partitionResult.second;

        if (begin < second) {
            simpleQSort(array, begin, second);
        }
        if (first < end) {
            simpleQSort(array, first, end);
        }
    }

    /**
     * Sorts array using qsort in one thread
     */
    public <T extends Comparable<? super T>> void simpleQSort(T[] array) {
        simpleQSort(array, 0, array.length - 1);
    }

    /**
     * Sorts array using mutlithread qsort
     */
    public <T extends Comparable<? super T>> void smartQSort(T[] array) throws InterruptedException {
        countDownLatch = new CountDownLatch(array.length);
        executorService.submit(new QSortRunner<>(array, 0, array.length - 1));
        try {
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }
    }
}
