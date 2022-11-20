package com.shd.cloud.iot.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(String.format(message));
    }
}