package com.closememo.query.infra.messaging.payload.document;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AutoTagsUpdatedEvent extends DomainEvent {

  private Identifier documentId;
  private List<String> autoTags;
}
