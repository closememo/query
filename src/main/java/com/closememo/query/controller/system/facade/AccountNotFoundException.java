package com.closememo.query.controller.system.facade;

import com.closememo.query.infra.exception.ResourceNotFoundException;

public class AccountNotFoundException extends ResourceNotFoundException {

  public AccountNotFoundException() {
  }

  public AccountNotFoundException(String message) {
    super(message);
  }
}
