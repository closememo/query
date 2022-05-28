package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.notification.NotificationActivatedEvent;
import com.closememo.query.infra.messaging.payload.notification.NotificationCreatedEvent;
import com.closememo.query.infra.messaging.payload.notification.NotificationDeletedEvent;
import com.closememo.query.infra.messaging.payload.notification.NotificationInactivatedEvent;
import com.closememo.query.infra.messaging.payload.notification.NotificationUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.notification.NotificationReadModel;
import com.closememo.query.infra.persistence.readmodel.notification.NotificationReadModel.Status;
import com.closememo.query.infra.persistence.readmodel.notification.NotificationReadModelRepository;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class NotificationDomainEventHandler {

  private final NotificationReadModelRepository repository;

  public NotificationDomainEventHandler(
      NotificationReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "NotificationCreatedEvent")
  public void handle(NotificationCreatedEvent payload) {
    NotificationReadModel.NotificationReadModelBuilder builder = NotificationReadModel.builder()
        .id(payload.getAggregateId())
        .title(payload.getTitle())
        .content(payload.getContent())
        .createdAt(payload.getCreatedAt())
        .notifyStart(payload.getNotifyStart())
        .notifyEnd(payload.getNotifyEnd())
        .status(payload.getStatus());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "NotificationUpdatedEvent")
  public void handle(NotificationUpdatedEvent payload) {
    NotificationReadModel notification = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    NotificationReadModel.NotificationReadModelBuilder builder = notification.toBuilder()
        .title(payload.getTitle())
        .content(payload.getContent())
        .notifyStart(payload.getNotifyStart())
        .notifyEnd(payload.getNotifyEnd());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "NotificationActivatedEvent")
  public void handle(NotificationActivatedEvent payload) {
    NotificationReadModel notification = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    NotificationReadModel.NotificationReadModelBuilder builder = notification.toBuilder()
        .status(Status.ACTIVE);

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "NotificationInactivatedEvent")
  public void handle(NotificationInactivatedEvent payload) {
    NotificationReadModel notification = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    NotificationReadModel.NotificationReadModelBuilder builder = notification.toBuilder()
        .status(Status.INACTIVE);

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "NotificationDeletedEvent")
  public void handle(NotificationDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }
}
