package org.stibits.rnft.errors;

public class AccountNotFound extends NotFoundError {
    public AccountNotFound () {
        super("account_found", "The account does not exist");
    }
}
