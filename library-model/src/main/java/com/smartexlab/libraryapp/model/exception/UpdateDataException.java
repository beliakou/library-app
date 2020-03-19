package com.smartexlab.libraryapp.model.exception;

/** Exception marking various errors occurred during data update. */
public class UpdateDataException extends RuntimeException {
    public UpdateDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
