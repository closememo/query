package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.difference.DifferenceCreatedEvent;
import com.closememo.query.infra.messaging.payload.difference.DifferenceDeletedEvent;
import com.closememo.query.infra.persistence.readmodel.difference.DifferenceReadModel;
import com.closememo.query.infra.persistence.readmodel.difference.DifferenceReadModel.LineDelta;
import com.closememo.query.infra.persistence.readmodel.difference.DifferenceReadModelRepository;
import java.util.List;
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
        .ownerId(Identifier.convertToString(payload.getOwnerId()))
        .documentId(Identifier.convertToString(payload.getDocumentId()))
        .documentVersion(payload.getDocumentVersion())
        .lineDeltas(payload.getLineDeltas())
        .createdAt(payload.getCreatedAt());

    setAdditionalProperties(builder, payload.getLineDeltas());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "DifferenceDeletedEvent")
  public void handle(DifferenceDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }

  private static void setAdditionalProperties(
      DifferenceReadModel.DifferenceReadModelBuilder builder, List<LineDelta> lineDeltas) {

    int inserted = 0;
    int deleted = 0;
    int changed = 0;
    for (LineDelta lineDelta : lineDeltas) {
      switch (lineDelta.getType()) {
        case CHANGE:
          changed += 1;
          break;
        case DELETE:
          deleted += 1;
          break;
        case INSERT:
          inserted += 1;
          break;
      }
    }
    builder
        .changed(changed)
        .deleted(deleted)
        .inserted(inserted);
  }
}
