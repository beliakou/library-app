package com.smartexlab.libraryapp.model.exception;

/** Generic exception used when no other exception matches */
public class ServerSideException extends RuntimeException {
    public ServerSideException(String message, Throwable cause) {
        super(message, cause);
    }
}
