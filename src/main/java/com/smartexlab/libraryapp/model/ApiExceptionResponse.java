package com.smartexlab.libraryapp.model;

public class ApiExceptionResponse {
    private String message;

    public ApiExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
