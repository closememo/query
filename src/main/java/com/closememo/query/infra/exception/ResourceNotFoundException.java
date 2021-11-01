package com.closememo.query.infra.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BusinessException {

  public ResourceNotFoundException() {
  }

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ResourceNotFoundException(Throwable cause) {
    super(cause);
  }

  public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

  @Override
  public boolean isNecessaryToLog() {
    return false;
  }
}
