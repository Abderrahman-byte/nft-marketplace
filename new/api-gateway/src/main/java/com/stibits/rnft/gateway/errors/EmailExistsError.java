package com.stibits.rnft.gateway.errors;

import com.stibits.rnft.common.errors.DuplicationError;

public class EmailExistsError extends DuplicationError {
    private static final String MESSAGE = "Account with the same email already exist.";

    public EmailExistsError () {
        super(MESSAGE, "email");
    }
}
