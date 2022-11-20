package com.shd.cloud.iot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {
    private final int statusCode;
    private final Date timestamp;
    private final String message;
    private final String description;
}