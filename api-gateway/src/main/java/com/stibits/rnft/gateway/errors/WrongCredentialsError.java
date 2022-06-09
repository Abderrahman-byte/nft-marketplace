package com.stibits.rnft.gateway.errors;

import com.stibits.rnft.common.errors.ApiError;

import org.springframework.http.HttpStatus;

public class WrongCredentialsError extends ApiError {
    public WrongCredentialsError () {
        super("wrong_credentials", "Invalid credentials", HttpStatus.BAD_REQUEST);
    }
}
