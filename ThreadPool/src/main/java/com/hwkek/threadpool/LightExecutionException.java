package com.hwkek.threadpool;

/**
 * Exception that throws in case of exception while supplier in threadpool task executing.
 */
public class LightExecutionException extends RuntimeException {
    private static final long serialVersionUID = 3995433511599130168L;

    /**
     * Creates exception by message
     */
    public LightExecutionException(String message) {
        super(message);
    }

    /**
     * Creates exception by message and cause
     */
    public LightExecutionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
