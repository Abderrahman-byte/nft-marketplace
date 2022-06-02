package org.stibits.rnft.errors;

import org.springframework.http.HttpStatus;

public class CollectionNotFound extends ApiError {
    public CollectionNotFound () {
        super("collection_not_found", "Collection with the given id does not exist", HttpStatus.NOT_FOUND);
    }
}
