package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.difference.DifferenceCreatedEvent;
import com.closememo.query.infra.messaging.payload.difference.DifferenceDeletedEvent;
import com.closememo.query.infra.persistence.readmodel.difference.DifferenceReadModel;
import com.closememo.query.infra.persistence.readmodel.difference.DifferenceReadModelRepository;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class DifferenceDomainEventHandler {

  private final DifferenceReadModelRepository repository;

  public DifferenceDomainEventHandler(DifferenceReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "DifferenceCreatedEvent")
  public void handle(DifferenceCreatedEvent payload) {
    DifferenceReadModel.DifferenceReadModelBuilder builder = DifferenceReadModel.builder()
        .id(payload.getAggregateId())
        .documentId(Identifier.convertToString(payload.getDocumentId()))
        .documentVersion(payload.getDocumentVersion())
        .lineDeltas(payload.getLineDeltas())
        .createdAt(payload.getCreatedAt());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "DifferenceDeletedEvent")
  public void handle(DifferenceDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }
}
