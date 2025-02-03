package com.localkart.services.store.api.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

public class Error {

    private HttpStatus httpStatus;
    private String message;
    private String target;

    public Error() {
    }

    public Error(HttpStatus httpStatus, String message, String target) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.target = target;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
