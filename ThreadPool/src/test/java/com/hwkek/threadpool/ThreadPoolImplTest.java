package com.hwkek.threadpool;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ThreadPoolImplTest {

    private volatile String reason;

    @Test
    void taskSimpleTest() throws LightExecutionException, TaskRejectedException, InterruptedException {
        int n = 10;
        final int value = 42;
        var threadPool = new ThreadPoolImpl<Integer>(n);
        var task = ThreadPoolImpl.createTask(() -> value);
        threadPool.add(task);
        int result = task.get();
        assertEquals(value, result);
        threadPool.shutdown();
    }

    @Test
    void simpleInterraptTest() {
        int kek = 1;
        int m = 2;
        var threadPool = new ThreadPoolImpl<Integer>(m);
        threadPool.shutdown();
        assertThrows(TaskRejectedException.class, () -> {threadPool.add(ThreadPoolImpl.createTask(() -> 42));});
    }

    @Test
    void addSimpleTest() throws TaskRejectedException, LightExecutionException, InterruptedException {
        int n = 100;
        int m = 1;
        var threadPool = new ThreadPoolImpl<String>(m);
        var tasks = new ArrayList<LightFuture<String>>();
        for (int i = 0; i < n; i++) {
            final int value = i;
            tasks.add(ThreadPoolImpl.createTask(() -> "Result " + value));
            threadPool.add(tasks.get(i));
        }
        for (int i = 0; i < n; i++) {
            var result = tasks.get(i).get();
            assertEquals("Result " + i, result);
        }
        threadPool.shutdown();
    }

    @Test
    void threadCountTest() {
        int n = 10;
        var threadPool = new ThreadPoolImpl<Boolean>(n);
        assertTrue(Thread.activeCount() >= n);
    }

    @Test
    void severalThreadSubmit() throws LightExecutionException, InterruptedException {
        int n = 10;
        int m = 5;
        var threadPool = new ThreadPoolImpl<String>(m);
        var threads = new Thread[n];
        var tasks = new ArrayList<LightFuture<String>>();
        for (int i = 0; i < n; i++) {
            final int value = i;
            tasks.add(ThreadPoolImpl.createTask(() -> {return "Result " + value; }));
            threads[i] = new Thread(() ->
            {
                try {
                    threadPool.add(tasks.get(value));
                } catch (TaskRejectedException e) {
                    reason = e.getMessage();
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < n; i++) {
            threads[i].join();
        }

        assertNull(reason);

        for (int i = 0; i < n; i++) {
            var result = tasks.get(i).get();
            assertEquals("Result " + i, result);
        }
        threadPool.shutdown();
    }

    @Test
    void severalThreadGet() throws InterruptedException, TaskRejectedException {
        int n = 10;
        int m = 5;
        var threadPool = new ThreadPoolImpl<Integer>(m);
        var threads = new Thread[n];
        var tasks = new ArrayList<LightFuture<Integer>>();
        for (int i = 0; i < n; i++) {
            final Integer value = i;
            tasks.add(ThreadPoolImpl.createTask(() -> {
                return value;
            }));
            threadPool.add(tasks.get(i));
            threads[i] = new Thread(() ->
            {
                try {
                    var kek = tasks.get(value).get();
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
        threadPool.shutdown();
    }

    @Test
    void severalThreadsWithOneTask() throws TaskRejectedException, LightExecutionException, InterruptedException {
        int m = 5;
        var threadPool = new ThreadPoolImpl<Integer>(m);
        var task = ThreadPoolImpl.createTask(() -> 42);
        for (int i = 0; i < m; i++) {
            threadPool.add(task);
        }
        int result = task.get();
        assertEquals(42, result);
        threadPool.shutdown();
    }

    @Test
    void simpleThenApplyTest() throws TaskRejectedException, LightExecutionException, InterruptedException {
        int m = 5;
        var threadPool = new ThreadPoolImpl<Integer>(m);
        var task = ThreadPoolImpl.createTask(() -> 42);
        var nextTask = task.thenApply(integer -> 10 * integer);
        threadPool.add(task);
        threadPool.add(nextTask);
        int result = task.get();
        int nextResult = nextTask.get();
        assertEquals(42, result);
        assertEquals(420, nextResult);
        threadPool.shutdown();
    }

    @Test
    void thenApplyThowableTest() throws TaskRejectedException, LightExecutionException, InterruptedException {
        int m = 5;
        var threadPool = new ThreadPoolImpl<String>(m);
        var task = ThreadPoolImpl.createTask(() -> {
            if (false) {
                return "AAAA";
            } else {
                throw new NullPointerException("kek");
            }
        });
        var nextTask = task.thenApply(string -> string + string);
        threadPool.add(task);
        threadPool.add(nextTask);

        assertThrows(LightExecutionException.class, nextTask::get);
        threadPool.shutdown();
    }
}