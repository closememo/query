package com.closememo.query.config.messaging.kafka;

public class KafkaMessageConverterException extends RuntimeException {

  public KafkaMessageConverterException(String message) {
    super(message);
  }

  public KafkaMessageConverterException(String message, Throwable cause) {
    super(message, cause);
  }
}
