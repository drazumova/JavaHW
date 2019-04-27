package com.hwkek.threadpool;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolImplTest {

    private volatile String reason;
    private ThreadPoolImpl threadPool;

    @Test
    void taskSimpleTest() throws LightExecutionException, TaskRejectedException, InterruptedException {
        int n = 10;
        final int value = 42;
        threadPool = new ThreadPoolImpl(n);
        var task = threadPool.add(() -> value);
        int result = task.get();
        assertEquals(value, result);
    }

    @Test
    void simpleInterruptTest() {
        int kek = 1;
        int m = 2;
        threadPool = new ThreadPoolImpl(m);
        threadPool.shutdown();
        assertThrows(TaskRejectedException.class, () -> {threadPool.add(() -> 42);});
    }

    @Test
    void differentTypesTest() throws TaskRejectedException, LightExecutionException, InterruptedException {
        int m = 2;
        threadPool = new ThreadPoolImpl(m);
        var task1 = threadPool.add(() -> "kek");
        var task2 = threadPool.add(() -> 42);
        //Cant use var because results will be optional
        int result2 = task2.get();
        var result1 = task1.get();
        assertEquals("kek", result1);
        assertEquals(42, result2);
    }

    @Test
    void addSimpleTest() throws TaskRejectedException, LightExecutionException, InterruptedException {
        int n = 100;
        int m = 1;
        threadPool = new ThreadPoolImpl(m);
        var tasks = new ArrayList<LightFuture<String>>();
        for (int i = 0; i < n; i++) {
            final int value = i;
            tasks.add(threadPool.add(() -> "Result " + value));
        }
        for (int i = 0; i < n; i++) {
            var result = tasks.get(i).get();
            assertEquals("Result " + i, result);
        }
    }

    @Test
    void threadCountTest() {
        int n = 10;
        threadPool = new ThreadPoolImpl(n);
        assertTrue(Thread.activeCount() >= n);
    }

    @Test
    void severalThreadGet() throws InterruptedException, TaskRejectedException {
        int n = 10;
        int m = 5;
        var threadPool = new ThreadPoolImpl(m);
        var threads = new Thread[n];
        var tasks = new ArrayList<LightFuture<Integer>>();
        for (int i = 0; i < n; i++) {
            final Integer value = i;
            tasks.add(threadPool.add(() -> value));
            final var task = tasks.get(i);
            threads[i] = new Thread(() ->
            {
                try {
                    var kek = task.get();
                    if (!kek.equals(value)) {
                        reason = "Expected " + value + " but " + kek + " found";
                    }
                } catch (Exception e) {
                    reason = e.getMessage();
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < n; i++) {
            threads[i].join();
        }
        assertNull(reason);
    }

    @Test
    void simpleThenApplyTest() throws TaskRejectedException, LightExecutionException, InterruptedException {
        int m = 5;
        threadPool = new ThreadPoolImpl(m);
        var task = threadPool.add(() -> 42);
        var nextTask = task.thenApply(integer -> 10 * integer);
        threadPool.addTask(nextTask);
        int result = task.get();
        int nextResult = nextTask.get();

        assertEquals(42, result);
        assertEquals(420, nextResult);
    }

    @Test
    void thenApplyThowableTest() throws TaskRejectedException {
        int m = 5;
        threadPool = new ThreadPoolImpl(m);
        var task = threadPool.add(() -> {
            if (false) {
                return "AAAA";
            } else {
                throw new NullPointerException();
            }
        });
        var nextTask = task.thenApply(string -> {
            return string + string;
        });
        threadPool.addTask(nextTask);
        assertThrows(LightExecutionException.class, nextTask::get);
    }
}