package com.stibits.rnft.gateway.errors;

import com.stibits.rnft.common.errors.DuplicationError;

public class UsernameExistsError extends DuplicationError {
    private static final String MESSAGE = "Account with the same username already exist.";

    public UsernameExistsError () {
        super(MESSAGE, "username");
    }
}
