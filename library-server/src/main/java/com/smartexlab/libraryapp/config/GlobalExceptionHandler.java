package com.smartexlab.libraryapp.config;

import com.smartexlab.libraryapp.model.exception.ReadDataException;
import com.smartexlab.libraryapp.model.exception.DataNotFoundException;
import com.smartexlab.libraryapp.model.exception.ServerSideException;
import com.smartexlab.libraryapp.model.response.ApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {DataNotFoundException.class})
    public ResponseEntity<ApiExceptionResponse> dataNotFound(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(value = {ReadDataException.class, ServerSideException.class})
    public ResponseEntity<ApiExceptionResponse> serverError(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiExceptionResponse(ex.getMessage()));
    }
}
