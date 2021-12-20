package com.closememo.query.infra.messaging.payload.category;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryCreatedEvent extends DomainEvent {

  private Identifier ownerId;
  private String name;
  private ZonedDateTime createdAt;
  private Boolean isRoot;
  private Identifier parentId;
  private Integer depth;
}
