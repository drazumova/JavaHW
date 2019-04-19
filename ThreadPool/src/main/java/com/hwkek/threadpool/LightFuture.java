package com.hwkek.threadpool;

import java.util.function.*;

/**
 * Interface of tasks given to thread pool
 * @param <T> return type
 */
public interface LightFuture<T> {
    /**
     * Tells if task has already complited
     */
    boolean isReady();

    /**
     * Returns result of executing
     * If result isnt counted yet, waits
     */
    T get() throws LightExecutionException;

    /**
     * Returns new Task that perform current Task and apply function to result
     */
    LightFuture<T> thenApply(Function<T, T> function);

    /**
     * Executes the task
     */
    void execute();
}
