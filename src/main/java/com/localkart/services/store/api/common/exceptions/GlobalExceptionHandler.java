package com.localkart.services.store.api.common.exceptions;

import com.localkart.services.store.api.common.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<Error> handleBusinessValidationException(BusinessValidationException ex) {

        String errorMessage = ex.getMessage();
        String target = ex.getTarget();
        HttpStatus httpStatus = ex.getHttpStatus();
        Error error = new Error(httpStatus, errorMessage, target);

        return ResponseEntity.status(httpStatus).body(error);
    }
}
