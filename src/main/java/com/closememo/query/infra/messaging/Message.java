package com.closememo.query.infra.messaging;

public abstract class Message {

  public abstract MessageType getMessageType();

  public enum MessageType {
    DOMAIN_EVENT, ACK_EVENT
  }
}
