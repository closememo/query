package com.closememo.query.infra.messaging.payload.notification;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.persistence.readmodel.notification.NotificationReadModel.Status;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationCreatedEvent extends DomainEvent {

  private String title;
  private String content;
  private ZonedDateTime createdAt;
  private ZonedDateTime notifyStart;
  private ZonedDateTime notifyEnd;
  private Status status;
}
