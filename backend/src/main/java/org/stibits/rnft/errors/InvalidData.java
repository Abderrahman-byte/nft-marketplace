package org.stibits.rnft.errors;

import org.springframework.http.HttpStatus;

public class InvalidData extends ApiError {
    public InvalidData () {
        super("invalid_data", "The Payload sent is invalid.", HttpStatus.BAD_REQUEST);
    }
}
