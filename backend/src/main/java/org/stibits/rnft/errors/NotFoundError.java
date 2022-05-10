package org.stibits.rnft.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

@JsonIncludeProperties({"title", "detail", "status", "timestamp"})
public class NotFoundError extends ApiError {
    public NotFoundError () {
        super("not_found", "The resource you're looking for does not exist", HttpStatus.NOT_FOUND);
    }

    public NotFoundError (String title, String details) {
        super(title, details, HttpStatus.NOT_FOUND);
    }
}
