package com.smartexlab.libraryapp.model.exception;

/** Exception marking various errors occurred during data access. */
public class ReadDataException extends RuntimeException {
    public ReadDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
