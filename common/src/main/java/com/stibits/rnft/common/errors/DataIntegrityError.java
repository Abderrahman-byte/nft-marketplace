package com.stibits.rnft.common.errors;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.Getter;
import lombok.Setter;

@JsonIncludeProperties({ "title", "detail", "timestamp", "status", "field" })
public class DataIntegrityError extends ApiError {
    @Getter @Setter private String field;

    public DataIntegrityError (String detail, String field) {
        super("data_integrity_error", detail, HttpStatus.BAD_REQUEST);
        this.field = field;
    }
}
