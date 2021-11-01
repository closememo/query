package com.closememo.query.infra.exception;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends RuntimeException {

  public BusinessException() {
  }

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessException(Throwable cause) {
    super(cause);
  }

  public BusinessException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public abstract HttpStatus getHttpStatus();

  public abstract boolean isNecessaryToLog();
}
