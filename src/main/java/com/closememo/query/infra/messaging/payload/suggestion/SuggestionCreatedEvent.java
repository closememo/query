package com.closememo.query.infra.messaging.payload.suggestion;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.persistence.readmodel.suggestion.SuggestionReadModel.Status;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuggestionCreatedEvent extends DomainEvent {

  private Identifier writerId;
  private String content;
  private ZonedDateTime createdAt;
  private Status status;
}
