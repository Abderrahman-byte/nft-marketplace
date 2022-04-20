package org.merchantech.nftproject.errors;

import org.springframework.http.HttpStatus;

public class AuthenticationRequiredError extends ApiError {
    public AuthenticationRequiredError () {
        super("authentication_required", "Authentication is required to perform this action", HttpStatus.UNAUTHORIZED);
    }
}
