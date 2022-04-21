package org.merchantech.nftproject.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

@JsonIncludeProperties({ "title", "status", "detail" , "timestamp" })
public class UnacceptedMediaTypeError extends ApiError {
    public UnacceptedMediaTypeError (String message) {
        super("unsupported_media_type", message, HttpStatus.BAD_REQUEST);        
    }

    public UnacceptedMediaTypeError (StorageUnacceptedMediaType ex) {
        this(ex.getMessage());
    }
}
