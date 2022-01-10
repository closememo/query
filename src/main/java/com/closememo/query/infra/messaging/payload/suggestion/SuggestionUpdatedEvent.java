package com.closememo.query.infra.messaging.payload.suggestion;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.persistence.readmodel.suggestion.SuggestionReadModel.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuggestionUpdatedEvent extends DomainEvent {

  private String content;
  private Status status;
}
