package com.closememo.query.infra.messaging.payload.bookmark;

import com.closememo.query.infra.messaging.DomainEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookmarkUpdatedEvent extends DomainEvent {

  private Integer bookmarkOrder;
}
