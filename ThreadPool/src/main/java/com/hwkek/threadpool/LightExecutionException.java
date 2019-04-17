package com.hwkek.threadpool;

public class LightExecutionException extends Exception {
    public LightExecutionException(String message){
        super(message);
    }

    public LightExecutionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
