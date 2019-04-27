package com.hwkek.threadpool;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

/**
 * Structure that executes all given tasks in several threads.
 */
public class ThreadPoolImpl {

    private class MyThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                synchronized (lockSimulator) {

                    while (queue.isEmpty()) {
                        try {
                            lockSimulator.wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                    var task = queue.poll();
                    if (task != null) {
                        task.execute();
                    }

            }
        }
    }

    private static class Task<T> implements LightFuture<T> {

        private @Nullable Supplier<T> supplier;
        private @Nullable T result;
        private @NotNull Throwable throwable;
        private final @NotNull Object lockSimulator;

        private Task(@NotNull Supplier<T> supplier) {
            lockSimulator = "Another kekos";
            this.supplier = supplier;
        }

        @Override
        public String toString() {
            return String.valueOf(supplier);
        }

        @Override
        public boolean isReady() {
            synchronized (lockSimulator) {
                return supplier == null;
            }
        }

        @Override
        public T get() throws LightExecutionException, InterruptedException {
            synchronized (lockSimulator) {
                while (supplier != null) {
                    lockSimulator.wait();
                }

                if (throwable != null) {
                    throw new LightExecutionException("Supplier ended with an exception " + throwable, throwable);
                } else {
                    return result;
                }
            }
        }

        @Override
        public void execute() {
            synchronized (lockSimulator) {
                if (supplier == null) {
                    return;
                }
                try {
                    result = supplier.get();
                } catch (Throwable e) {
                    throwable = e;
                }
                supplier = null;
                lockSimulator.notifyAll();
            }
        }

        @Override
        public <U> LightFuture<U> thenApply(@NotNull Function<? super T, U> function) {
            return new Task<U>(() -> {
                try {
                    return function.apply(get());
                } catch (InterruptedException e) {
                    throw new LightExecutionException("Inner task ended with exception " + e, e);
                }
            });
        }
    }

    private final @NotNull Object lockSimulator;
    private final Queue<LightFuture<?>> queue;
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
     * Does not wait until all tasks are completed.
     */
    public void shutdown() {
        synchronized (lockSimulator) {
            isClosed = true;
            for (int i = 0; i < size; i++) {
                threads[i].interrupt();
            }
        }
    }

    /**
     * Adds task of form of LightFuture interface to queue for executing.
     * @return LightFuture in which it is wrapped
     */
    public <T> LightFuture<T> addTask(LightFuture<T> task) throws TaskRejectedException {
        synchronized (lockSimulator) {
            if (isClosed) {
                throw new TaskRejectedException("Pool is closed");
            }
            queue.add(task);
            lockSimulator.notifyAll();
            return task;
        }
    }

    /**
     * Adds task in form of supplier to queue for executing.
     * If pool is closed, throws exception.
     * @return LightFuture in which it is wrapped
     */
    public <T> LightFuture<T> add(@NotNull Supplier<T> task) throws TaskRejectedException {
        var taskForPool = new Task<>(task);
        return addTask(taskForPool);
    }
}
