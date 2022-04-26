package com.closememo.query.infra.messaging.payload.document;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentDeletedEvent extends DomainEvent {

  private Identifier documentId;
}
