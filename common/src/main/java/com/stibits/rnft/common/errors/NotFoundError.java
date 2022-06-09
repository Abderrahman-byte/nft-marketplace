package com.stibits.rnft.common.errors;

import org.springframework.http.HttpStatus;

public class NotFoundError extends ApiError {
    private static final String DEFAULT_MESSAGE = "The resource you are looking for does not exist."    ;

    public NotFoundError () {
        this(DEFAULT_MESSAGE);
    }

    public NotFoundError (String detail) {
        this("not_found", detail);
    }

    public NotFoundError (String title, String detail) {
        super(title, detail, HttpStatus.NOT_FOUND);
    }
}
