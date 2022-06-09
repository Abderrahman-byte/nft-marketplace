package com.stibits.rnft.gateway.errors;

import com.stibits.rnft.common.errors.ApiError;

import org.springframework.http.HttpStatus;

public class InactivatedAccountError extends ApiError {
    private static final String MESSAGE = "You're account is not activated, please check your inbox for a verification mail.";

    public InactivatedAccountError () {
        super("inactive_account", MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
