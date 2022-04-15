package org.merchantech.nftproject.controllers;

import java.util.HashMap;
import java.util.Map;

import org.merchantech.nftproject.errors.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestErrorHandler {
    @ExceptionHandler(ApiError.class)
    public ResponseEntity<Object> handleApiError (ApiError error) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", error);

        return new ResponseEntity<>(response, HttpStatus.valueOf(error.getStatus()));
    }
}
