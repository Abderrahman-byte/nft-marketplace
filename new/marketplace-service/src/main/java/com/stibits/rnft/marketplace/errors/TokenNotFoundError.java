package com.stibits.rnft.marketplace.errors;

import com.stibits.rnft.common.errors.NotFoundError;

public class TokenNotFoundError extends NotFoundError {
    public TokenNotFoundError () {
        super("token_not_found", "The token you are looking for does not exist.");
    }    
}
