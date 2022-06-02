package com.stibits.rnft.common.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@JsonIncludeProperties({ "title", "detail", "timestamp", "status", "field" })
public class DuplicationError extends ApiError {
    @Getter @Setter private String field;

    public DuplicationError (String detail, String field) {
        super("duplication_error", detail, HttpStatus.BAD_REQUEST);
        this.field = field;
    }
}
