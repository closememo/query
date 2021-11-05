package com.closememo.query.infra.elasticsearch;

import com.closememo.query.infra.exception.InternalServerException;

public class ElasticsearchClientException extends InternalServerException {

  private static final long serialVersionUID = 2518635422787326580L;

  public ElasticsearchClientException() {
  }

  public ElasticsearchClientException(String message) {
    super(message);
  }
}
