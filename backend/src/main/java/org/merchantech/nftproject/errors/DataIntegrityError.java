package org.merchantech.nftproject.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

@JsonIncludeProperties({ "title", "status", "detail" , "timestamp", "field" })
public class DataIntegrityError extends ApiError {
    private String field;
    
    public DataIntegrityError (String message, String field) {
        this.setDetail(message);
        this.setTitle("data_integrety_error");
        this.setStatus(HttpStatus.BAD_REQUEST);
        this.setTimestamp(System.currentTimeMillis());
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
