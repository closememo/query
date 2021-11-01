package com.closememo.query.infra.messaging.payload.document;

import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.DomainEvent;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentCreatedEvent extends DomainEvent {

  private Identifier ownerId;
  private String title;
  private String content;
  private List<String> tags;
  private ZonedDateTime createdAt;
}
