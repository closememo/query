package com.closememo.query.infra.messaging;

import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AckEvent extends Message implements Serializable {

  private String aggregateId;
  private int eventVersion;
  private ZonedDateTime occurredOn;

  public AckEvent(String aggregateId, int eventVersion) {
    this.aggregateId = aggregateId;
    this.eventVersion = eventVersion;
    this.occurredOn = ZonedDateTime.now();
  }

  @Override
  public MessageType getMessageType() {
    return MessageType.ACK_EVENT;
  }
}
