package com.stibits.rnft.common.errors;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIncludeProperties({ "title", "detail", "timestamp", "status" })
public class ApiError extends Throwable {
    private String title;
    private String detail;
    private Calendar timestamp = Calendar.getInstance();
    private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public ApiError (String title, String detail, HttpStatus status) {
        this(title, detail);
        this.status = status.value();
    }

    public ApiError (String title, String detail) {
        this.title = title;
        this.detail = detail;
    }
}
