package com.closememo.query.infra.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BusinessException {

  public AccessDeniedException() {
  }

  public AccessDeniedException(String message) {
    super(message);
  }

  public AccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

  public AccessDeniedException(Throwable cause) {
    super(cause);
  }

  public AccessDeniedException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  @Override
  public HttpStatus getHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }

  @Override
  public boolean isNecessaryToLog() {
    return false;
  }
}
