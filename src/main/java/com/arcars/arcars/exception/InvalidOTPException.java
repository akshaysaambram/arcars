package com.arcars.arcars.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidOTPException extends RuntimeException {
    private String errorMessage;

    public InvalidOTPException(String errorMessage) {
        super(String.format("%s", errorMessage));
        this.errorMessage = errorMessage;
    }
}
