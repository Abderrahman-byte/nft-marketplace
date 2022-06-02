package org.stibits.rnft.errors;

import org.springframework.http.HttpStatus;

public class UnverifiedEmailError extends ApiError {
    public UnverifiedEmailError () {
        super("unverified_email", "You must verify your email in order to login", HttpStatus.BAD_REQUEST);
    }
}
