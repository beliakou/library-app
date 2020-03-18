package com.smartexlab.libraryapp.model.exception;

/** Exception to use when no data found in database */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
