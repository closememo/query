package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.suggestion.SuggestionCreatedEvent;
import com.closememo.query.infra.messaging.payload.suggestion.SuggestionDeletedEvent;
import com.closememo.query.infra.messaging.payload.suggestion.SuggestionDeletedStatusSetEvent;
import com.closememo.query.infra.messaging.payload.suggestion.SuggestionUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.suggestion.SuggestionReadModel;
import com.closememo.query.infra.persistence.readmodel.suggestion.SuggestionReadModel.Status;
import com.closememo.query.infra.persistence.readmodel.suggestion.SuggestionReadModelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class SuggestionDomainEventHandler {

  private static final int PREVIEW_LIMIT = 150;

  private final SuggestionReadModelRepository repository;

  public SuggestionDomainEventHandler(
      SuggestionReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "SuggestionCreatedEvent")
  public void handle(SuggestionCreatedEvent payload) {
    SuggestionReadModel.SuggestionReadModelBuilder builder = SuggestionReadModel.builder()
        .id(payload.getAggregateId())
        .writerId(Identifier.convertToString(payload.getWriterId()))
        .content(payload.getContent())
        .createdAt(payload.getCreatedAt())
        .status(payload.getStatus());
    setAdditionalProperties(builder, payload.getContent());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "SuggestionUpdatedEvent")
  public void handle(SuggestionUpdatedEvent payload) {
    SuggestionReadModel suggestion = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    SuggestionReadModel.SuggestionReadModelBuilder builder = suggestion.toBuilder()
        .content(payload.getContent())
        .status(payload.getStatus());
    setAdditionalProperties(builder, payload.getContent());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "SuggestionDeletedEvent")
  public void handle(SuggestionDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }

  @ServiceActivator(inputChannel = "SuggestionDeletedStatusSetEvent")
  public void handle(SuggestionDeletedStatusSetEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(suggestion -> {
          SuggestionReadModel.SuggestionReadModelBuilder builder = suggestion.toBuilder()
              .status(Status.DELETED);
          repository.save(builder.build());
        });
  }

  private static void setAdditionalProperties(
      SuggestionReadModel.SuggestionReadModelBuilder builder, String content) {

    String replacedContent = content.replaceAll("[\\r\\n]+", " ");
    builder.preview(substringPreview(replacedContent, PREVIEW_LIMIT));
  }

  private static String substringPreview(String plainText, int previewLimit) {
    return StringUtils.length(plainText) <= previewLimit
        ? plainText
        : plainText.substring(0, plainText.offsetByCodePoints(0, previewLimit)) + "...";
  }
}
