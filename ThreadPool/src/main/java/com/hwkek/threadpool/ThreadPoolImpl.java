package com.hwkek.threadpool;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

/**
 * Structure that executes all given tasks in several threads.
 * @param <T> return type of tasks.
 */
public class ThreadPoolImpl<T> {

    private class MyThread extends Thread {
        @Override
        public void run() {
            LightFuture<T> task = null;
            while (!Thread.interrupted()) {
                synchronized (lockSimulator) {
                    if (!queue.isEmpty()) {
                        task = queue.poll();
                    }
                }
                if (task != null) {
                    task.execute();
                    task = null;
                }
            }
        }
    }

    private static class Task<T> implements LightFuture<T> {

        private @Nullable Supplier<T> supplier;
        private @Nullable T result;
        private @NotNull Throwable throwable;

        private Task(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public boolean isReady() {
            return supplier == null;
        }

        @Override
        public T get() throws LightExecutionException {
            while (supplier != null) {
                Thread.yield();
            }

            if (throwable != null) {
                throw new LightExecutionException("Supplier ended with an axception " + throwable, throwable);
            } else {
                return result;
            }
        }

        @Override
        public void execute() {
            if (supplier == null) {
                return;
            }

            try {
                result = supplier.get();
            } catch (Throwable e) {
                throwable = e;
            }
            supplier = null;
        }

        @Override
        public LightFuture<T> thenApply(@NotNull Function<T, T> function) {
            return new Task<>(() -> {
                if (supplier != null) {
                    result = supplier.get();
                    supplier = null;
                }
                return function.apply(result);
            });
        }
    }

    private final @NotNull Object lockSimulator;
    private final Queue<LightFuture<T>> queue;
    private final int size;
    private final Thread[] threads;
    private boolean isClosed;

    /**
     * Creates new thread pool with fixed number of threads
     * @param size number of threads
     */
    public ThreadPoolImpl(int size) {
        this.size = size;
        isClosed = false;
        lockSimulator = "KEKOS";
        threads = new Thread[size];
        queue = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            threads[i] = new MyThread();
            threads[i].start();
        }
    }

    /**
     * Interrupts all threads
     */
    public void shutsown() {
        synchronized (lockSimulator) {
            isClosed = true;
            for (int i = 0; i < size; i++) {
                threads[i].interrupt();
            }
        }
    }

    /**
     * Adds task to queue for executing
     * If pool is closed, throws exception.
     */
    public void add(@NotNull LightFuture<T> task) throws TaskRejectedException {
        synchronized (lockSimulator) {
            if (isClosed) {
                throw new TaskRejectedException("Pool is closed");
            }
            queue.add(task);
        }
    }

    /**
     * Creates default implementation instance of thread pool task.
     */
     public static <T> LightFuture<T> createTask(@NotNull Supplier<T> supplier) {
        return new Task<>(supplier);
    }
}
