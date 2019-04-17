package com.hwkek.threadpool;

import java.util.*;
import java.util.concurrent.locks.*;
import java.util.function.*;

public class ThreadPoolImpl<T> {

    private ReentrantLock lock;
    private Condition notEmpty;
    private Queue<Task> queue;
    private int size;
    private final Thread[] threads;

    public ThreadPoolImpl(int size) {
        this.size = size;
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new MyThread();
            threads[i].start();
        }
    }

    private class Task implements LightFuture<T> {
        private Supplier<T> supplier;
        private T result;

        Task(Supplier<T> supplier) {
            this.supplier = supplier;

        }

        @Override
        public boolean isReady() {
            return supplier == null;
        }

        @Override
        public T get() throws LightExecutionException {
            if (supplier != null) {
                try {
                    result = supplier.get();
                } catch (Exception e) {
                    throw new LightExecutionException("Supplier ended with an axception", e);
                }
                supplier = null;
            }
            return result;
        }

        @Override
        public <E> LightFuture<E> thenApply(Function<T, E> function) {
            return null;
        }

    }

    public class MyThread extends Thread {
        private Task task;

        @Override
        public void run() {
            lock.lock();
            try {
                if (task.isReady()) {
                    while (queue.isEmpty()) {
                        notEmpty.await();
                    }
                    task = queue.poll();
                }
            } catch (InterruptedException e) {
               //kek
            } finally {
                lock.unlock();
            }
        }
    }

}
