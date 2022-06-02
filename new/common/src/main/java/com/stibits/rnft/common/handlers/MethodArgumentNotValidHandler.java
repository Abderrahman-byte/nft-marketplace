package com.stibits.rnft.common.handlers;

import com.stibits.rnft.common.api.ApiFailureResponse;
import com.stibits.rnft.common.errors.ApiError;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class MethodArgumentNotValidHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiFailureResponse<ApiError>> handleException (MethodArgumentNotValidException ex ) {
        ApiError error = new ApiError("MethodArgumentNotValidException", ex.getMessage());

        error.setStatus(400);

        return new ResponseEntity<>(new ApiFailureResponse<ApiError>(error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiFailureResponse<ApiError>> handleException (Exception ex) {
        ApiError error = new ApiError("WebExchangeBindException", ex.getMessage());

        error.setStatus(400);

        return new ResponseEntity<>(new ApiFailureResponse<ApiError>(error), HttpStatus.BAD_REQUEST);
    }
}
