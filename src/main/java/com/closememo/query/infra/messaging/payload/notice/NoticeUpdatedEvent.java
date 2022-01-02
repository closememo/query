package com.closememo.query.infra.messaging.payload.notice;

import com.closememo.query.infra.messaging.DomainEvent;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeUpdatedEvent extends DomainEvent {

  private String title;
  private String content;
  private ZonedDateTime updatedAt;
}
