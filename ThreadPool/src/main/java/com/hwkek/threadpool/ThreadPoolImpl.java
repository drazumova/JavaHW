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
            LightFuture<Object> task = null;
            while (!Thread.interrupted()) {
                synchronized (lockSimulator) {
                    while (queue.isEmpty()) {
                        try {
                            lockSimulator.wait();
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    task = queue.poll();
                }
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
            return new Task<>(() -> {
                try {
                    return function.apply(get());
                } catch (Exception e) {
                    throw new NullPointerException();
                }
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
     * Adds task to queue for executing
     * If pool is closed, throws exception.
     */
    public <T> void add(@NotNull Supplier<T> task) throws TaskRejectedException {
        synchronized (lockSimulator) {
            if (isClosed) {
                throw new TaskRejectedException("Pool is closed");
            }
            queue.add(new Task<T>(task));
            lockSimulator.notify();
        }
    }

    /**
     * Creates default implementation instance of thread pool task.
     */
     public static <T> LightFuture<T> createTask(@NotNull Supplier<T> supplier) {
        return new Task<>(supplier);
    }
}
