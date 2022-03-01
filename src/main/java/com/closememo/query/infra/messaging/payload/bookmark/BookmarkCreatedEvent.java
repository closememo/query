package com.closememo.query.infra.messaging.payload.bookmark;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookmarkCreatedEvent extends DomainEvent {

  private Identifier ownerId;
  private Identifier documentId;
  private Integer bookmarkOrder;
  private ZonedDateTime createdAt;
}
