package org.stibits.rnft.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

@JsonIncludeProperties({ "title", "detail", "status", "timestamp" })
public class UnknownError extends ApiError {
    public UnknownError () {
        super("unkown_error", "Something went wrong, please try again another time", HttpStatus.BAD_REQUEST);
    }
}
