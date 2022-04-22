package org.stibits.rnft.errors;

import org.springframework.http.HttpStatus;

public class WrongCredentialsError extends ApiError {
    public WrongCredentialsError () {
        super("wrong_credentials", "Username or password is incorrect", HttpStatus.BAD_REQUEST);
    }
}
