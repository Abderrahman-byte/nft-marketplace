package org.merchantech.nftproject.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

@JsonIncludeProperties({ "title", "detail", "status", "timestamp" })
public class UnknownError extends ApiError {
    public UnknownError () {
        this.setTitle("unkown_error");
        this.setDetail("Something went wrong, please try again another time");
        this.setStatus(HttpStatus.BAD_REQUEST);
        this.setTimestamp(System.currentTimeMillis());
    }
}
