package com.hw.qsort;

import org.jetbrains.annotations.*;

import java.util.concurrent.*;

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

        private void swap(int i, int j) {
            T tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }

        private QSortRunner(T[] array, int begin, int end) {
            this.begin = begin;
            this.end = end;
            this.array = array;

        }

        @Override
        public void run() {
            int first = begin;
            int second = end;
            T middle = array[(end + begin) / 2];

            synchronized (array) {
                while (first <= second) {
                    while (array[first].compareTo(middle) < 0) {
                        first++;
                    }
                    while (array[second].compareTo(middle) > 0) {
                        second--;
                    }
                    if (first <= second) {
                        swap(first, second);
                        first++;
                        second--;
                    }
                }
            }
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

    private <T extends Comparable<? super T>> void SimpleQSort(T[] array, int begin, int end) {
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
                T temp = array[first];
                array[first] = array[second];
                array[second] = temp;
                first++;
                second--;
            }
        }
        if (begin < second) {
            SimpleQSort(array, begin, second);
        }
        if (first < end) {
            SimpleQSort(array, first, end);
        }
    }

    /**
     * Sorts array using qsort in one thread
     */
    public <T extends Comparable<? super T>> void SimpleQSort(T[] array) {
        SimpleQSort(array, 0, array.length - 1);
    }

    /**
     * Sorts array using mutlithread qsort
     */
    public <T extends Comparable<? super T>> void SmartQSort(T[] array) throws InterruptedException {
        countDownLatch = new CountDownLatch(array.length);
        executorService.submit(new QSortRunner<>(array, 0,array.length - 1));
        try {
            countDownLatch.await();
        } finally {
            executorService.shutdown();
        }
    }
}
