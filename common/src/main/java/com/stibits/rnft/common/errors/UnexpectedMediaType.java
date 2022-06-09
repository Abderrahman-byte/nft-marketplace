package com.stibits.rnft.common.errors;

import org.springframework.http.HttpStatus;

public class UnexpectedMediaType extends ApiError {
    private static final String DEFAULT_MESSAGE = "Unexpected media type.";

    public UnexpectedMediaType () {
        this(DEFAULT_MESSAGE);
    }

    public UnexpectedMediaType (String detail) {
        super("unexpected_media_type", detail, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
