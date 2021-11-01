package com.closememo.query.infra.messaging.payload.difference;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DifferenceDeletedEvent extends DomainEvent {

  private Identifier differenceId;
}
