package com.stibits.rnft.common.errors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIncludeProperties({ "title", "detail", "timestamp", "status", "invalidFields" })
public class InvalidFieldsError extends ApiError {
    private static final String DETAIL = "Your request fields didn't validate.";

    @Getter @Setter private List<InvalidField> invalidFields = new ArrayList<>();

    public InvalidFieldsError (List<InvalidField> invalidFields) {
        this();
        this.invalidFields.addAll(invalidFields);
    }

    public InvalidFieldsError () {
        super("invalid_fields_error", DETAIL, HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InvalidField {
        private String field;
        private String reason;
    }
}
