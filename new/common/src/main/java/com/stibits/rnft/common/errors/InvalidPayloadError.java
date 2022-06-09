package com.stibits.rnft.common.errors;

import org.springframework.http.HttpStatus;

public class InvalidPayloadError extends ApiError {
    public InvalidPayloadError () {
        super("invalid_payload", "The payload received is invalid.", HttpStatus.BAD_REQUEST);
    }
}
