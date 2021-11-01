package com.closememo.query.infra.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DomainEvent extends Message implements Serializable {

  protected String aggregateId;
  protected int eventVersion;
  protected ZonedDateTime occurredOn;

  @JsonIgnore
  @Override
  public MessageType getMessageType() {
    return MessageType.DOMAIN_EVENT;
  }
}
