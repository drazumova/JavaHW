package com.hwkek.threadpool;

/**
 *Exception in case of trying to add task to closed thread pool
 */
public class TaskRejectedException extends Exception {

    private static final long serialVersionUID = -2898187844062072527L;

    /**
     *Creates exception by message and reason
     */
    public TaskRejectedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Creates exception by message
     */
    public TaskRejectedException(String message) {
        super(message);
    }
}
