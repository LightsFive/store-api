package com.localkart.services.store.api.common.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

public class BusinessValidationException extends RuntimeException {

    String message;
    HttpStatus httpStatus;
    String target;

    public BusinessValidationException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
