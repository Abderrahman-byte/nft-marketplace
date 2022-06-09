package com.stibits.rnft.common.handlers;

import com.stibits.rnft.common.api.ApiFailureResponse;
import com.stibits.rnft.common.api.ApiResponse;
import com.stibits.rnft.common.errors.ApiError;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiErrorHandler {
    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ApiResponse> handleApiError (ApiError ex) {
        return new ResponseEntity<>(new ApiFailureResponse<ApiError>(ex), HttpStatus.valueOf(ex.getStatus()));
    }
}
