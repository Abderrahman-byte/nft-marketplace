package com.stibits.rnft.marketplace.errors;

import com.stibits.rnft.common.errors.ApiError;

public class CollectionNotFoundError extends ApiError {
    public CollectionNotFoundError () {
        super("collection_not_found", "The collection you are looking for does not exist.");
    }
}
