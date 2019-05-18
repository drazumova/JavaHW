package com.hwkek.threadpool;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.*;

/**
 * Structure that executes all given tasks in several threads.
 */
public class ThreadPoolImpl {

    private static class MyQueue<T> {

        private @NotNull Object[] queue;
        private volatile int size;

        private MyQueue(int size) {
            queue = new Object[size];
            this.size = 0;
        }

        private int size() {
            return size;
        }

        private void resize() {
            int sizeArray = queue.length;
            var oldQueue = new Object[sizeArray];
            System.arraycopy(queue, 0, oldQueue, 0, sizeArray);
            queue = new Object[2 * sizeArray];
            System.arraycopy(oldQueue, 0, queue, 0, sizeArray);
        }

        private synchronized void add(@NotNull T t) {
            if (size + 1 > queue.length) {
                resize();
            }
            queue[size] = t;
            size++;
        }

        @SuppressWarnings("unchecked")
        private synchronized T get() {
            if (size == 0) {
                return null;
            }
            --size;
            return (T) queue[size];
        }
    }

    private class MyThread extends Thread {

        @Override
        public void run() {
            while (true) {
                LightFuture<?> task;
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                    task = queue.get();
                }
                if (task != null) {
                    task.execute();
                }
                synchronized (queue) {
                    if (queue.size() == 0 && isClosed) {
                        return;
                    }
                }
            }
        }
    }

    private class Task<T> implements LightFuture<T> {

        private volatile @Nullable Supplier<T> supplier;
        private @Nullable T result;
        private @NotNull Throwable throwable;
        private final @NotNull Object lock;
        private final @NotNull MyQueue<Task<?>> waitingQueue;

        private Task(@NotNull Supplier<T> supplier) {
            lock = "Another kekos";
            this.supplier = supplier;
            waitingQueue = new MyQueue<>(TASK_COUNT);
        }

        @Override
        public boolean isReady() {
            return supplier == null;
        }

        @Override
        public T get() throws LightExecutionException, InterruptedException {
            synchronized (lock) {
                while (supplier != null) {
                    lock.wait();
                }
            }
            if (throwable != null) {
                throw new LightExecutionException("Supplier ended with an exception " + throwable, throwable);
            } else {
                return result;
            }
        }

        @Override
        public void execute(){
            synchronized (lock) {
                if (supplier == null) {
                    return;
                }
                try {
                    result = supplier.get();
                } catch (Throwable e) {
                    throwable = e;
                }
                supplier = null;
                lock.notifyAll();
                while (waitingQueue.size != 0) {
                    var task = waitingQueue.get();
                    try {
                        addTask(task);
                    } catch (TaskRejectedException e) {
                        synchronized (task.lock) {
                            task.supplier = null;
                            task.throwable = new LightExecutionException("Can not apply when pool is closed", e);
                            task.lock.notifyAll();
                        }
                    }
                }
            }
        }

        @Override
        public <U> LightFuture<U> thenApply(@NotNull Function<? super T, U> function) {


            synchronized (lock) {
                if (supplier != null) {
                    var task =  new Task<>(() -> {
                        try {
                            return function.apply(get());
                        } catch (Exception e) {
                            throw new LightExecutionException("Inner task ended with exception " + e, e);
                        }
                    });
                    waitingQueue.add(task);
                    return task;
                }
            }
            var task = new Task<>(() -> {
                try {
                    if (throwable != null){
                        throw throwable;
                    }
                    return function.apply(result);
                } catch (Throwable e) {
                    throw new LightExecutionException("Inner task ended with exception " + e, e);
                }
            });
            addTask(task);

            return task;
        }
    }

    private final MyQueue<LightFuture<?>> queue;
    private final int size;
    private final Thread[] threads;
    private boolean isClosed;
    private static final int TASK_COUNT = 100;

    /**
     * Creates new thread pool with fixed number of threads
     * @param size number of threads
     */
    public ThreadPoolImpl(int size) {
        this.size = size;
        isClosed = false;
        threads = new Thread[size];
        queue = new MyQueue<>(size * TASK_COUNT);
        for (int i = 0; i < size; i++) {
            threads[i] = new MyThread();
            threads[i].start();
        }
    }

    /**
     * Interrupts all threads
     * Waits until all tasks are completed.
     */
    public void shutdown() throws InterruptedException {
        synchronized (queue) {
            if (isClosed) {
                return;
            }
            isClosed = true;
        }
        for (int i = 0; i < size; i++) {
            threads[i].interrupt();
            threads[i].join();
        }
    }

    private <T> LightFuture<T> addTask(LightFuture<T> task) throws TaskRejectedException {
        synchronized (queue) {
            if (isClosed) {
                throw new TaskRejectedException("Pool is closed");
            }
            queue.add(task);
            queue.notify();
            return task;
        }
    }

    /**
     * Adds task in form of supplier to queue for executing.
     * If pool is closed, throws exception.
     * @return LightFuture in which it is wrapped
     */
    public <T> LightFuture<T> add(@NotNull Supplier<T> task) throws TaskRejectedException {
        synchronized (queue) {
            if (isClosed) {
                throw new TaskRejectedException("Pool is closed");
            }
            var taskForPool = new Task<>(task);
            queue.add(taskForPool);
            queue.notify();
            return taskForPool;
        }

    }
}
