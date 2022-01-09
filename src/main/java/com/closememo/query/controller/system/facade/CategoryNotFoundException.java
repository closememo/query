package com.closememo.query.controller.system.facade;

import com.closememo.query.infra.exception.ResourceNotFoundException;

public class CategoryNotFoundException extends ResourceNotFoundException {

  public CategoryNotFoundException() {
  }

  public CategoryNotFoundException(String message) {
    super(message);
  }
}
