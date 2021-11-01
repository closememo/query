package com.closememo.query.controller.shared.errorhandler;

import com.closememo.query.infra.exception.BusinessException;
import java.util.Optional;
import javax.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = {
    "com.closememo.query.controller"
})
public class RestControllerExceptionHandler {

  @ExceptionHandler({
      BusinessException.class
  })
  protected ResponseEntity<ErrorResponse> handle(BusinessException exception) {
    if (exception.isNecessaryToLog()) {
      String msg = Optional.ofNullable(exception.getMessage()).orElse(exception.getClass().getSimpleName());
      log.error(msg, exception);
    }

    Error error = new Error(exception.getClass().getSimpleName(), exception.getMessage());
    return new ResponseEntity<>(new ErrorResponse(error), new HttpHeaders(), exception.getHttpStatus());
  }

  @ExceptionHandler({
      NoResultException.class
  })
  protected ResponseEntity<ErrorResponse> handleNotFound(Exception exception) {
    Error error = new Error(exception.getClass().getSimpleName(), exception.getMessage());
    return new ResponseEntity<>(new ErrorResponse(error), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }
}
