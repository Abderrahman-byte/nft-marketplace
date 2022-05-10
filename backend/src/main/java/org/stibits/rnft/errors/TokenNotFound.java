package org.stibits.rnft.errors;

public class TokenNotFound extends NotFoundError {
    public TokenNotFound () {
        super("token_not_found", "The token with this id does not exist.");
    }
}
