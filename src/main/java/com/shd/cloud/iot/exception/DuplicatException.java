package com.shd.cloud.iot.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DuplicatException(String confield) {
    super(String.format(confield));
  }
}
