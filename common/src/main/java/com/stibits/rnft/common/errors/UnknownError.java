package com.stibits.rnft.common.errors;

import org.springframework.http.HttpStatus;

public class UnknownError extends ApiError {
    public UnknownError () {
        super("unknown_error", "Something went wrong, please try another time", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
