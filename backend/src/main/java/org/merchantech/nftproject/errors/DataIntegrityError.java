package org.merchantech.nftproject.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

@JsonIncludeProperties({ "title", "status", "detail" , "timestamp", "field" })
public class DataIntegrityError extends ApiError {
    private String field;
    
    public DataIntegrityError (String message, String field) {
        super("data_integrety_error", message, HttpStatus.BAD_REQUEST);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
