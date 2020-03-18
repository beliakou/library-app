package com.smartexlab.libraryapp.model.exception;

/** Exception marking various errors occurred during data access. */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
