package org.stibits.rnft.errors;

import org.springframework.http.HttpStatus;

public class UnauthorizedError extends ApiError {
    public UnauthorizedError () {
        super("unauthorized", "Your account is unauthorized for this action", HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedError (String title, String details) {
        super(title, details, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedError (String details) {
        super("unauthorized", details, HttpStatus.UNAUTHORIZED);
    }
}
