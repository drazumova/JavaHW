package com.hwkek.threadpool;

import java.util.function.*;

public interface LightFuture<T> {
    boolean isReady();
    T get() throws LightExecutionException;
    <E> LightFuture<E> thenApply(Function<T, E> function);
}
