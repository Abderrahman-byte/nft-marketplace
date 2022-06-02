package org.stibits.rnft.errors;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import org.springframework.http.HttpStatus;

@JsonIncludeProperties({ "title", "status", "detail" , "timestamp" })
public class ApiError extends Throwable {
    private String title;
    private int status;
    private String detail;
    private long timestamp;

    public ApiError () {}

    public ApiError (String title, String detail, HttpStatus status) {
        this.title = title;
        this.detail = detail;
        this.status = status.value();
        this.timestamp = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String getMessage() {
        return "[" + this.status + "] " + this.title + " : " + this.detail;
    }
}
