package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.document.AutoTagsUpdatedEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentCreatedEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentDeletedEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModel;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModelRepository;
import com.closememo.query.infra.exception.ResourceNotFoundException;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocumentDomainEventHandler {

  private static final int PREVIEW_LIMIT = 150;

  private final DocumentReadModelRepository repository;

  public DocumentDomainEventHandler(DocumentReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "DocumentCreatedEvent")
  public void handle(DocumentCreatedEvent payload) {
    DocumentReadModel.DocumentReadModelBuilder builder = DocumentReadModel.builder()
        .id(payload.getAggregateId())
        .ownerId(Identifier.convertToString(payload.getOwnerId()))
        .categoryId(Identifier.convertToString(payload.getCategoryId()))
        .title(payload.getTitle())
        .content(payload.getContent())
        .tags(payload.getTags())
        .createdAt(payload.getCreatedAt())
        .option(payload.getOption());
    setAdditionalProperties(builder, payload.getContent());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "DocumentUpdatedEvent")
  public void handle(DocumentUpdatedEvent payload) {
    DocumentReadModel document = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    DocumentReadModel.DocumentReadModelBuilder builder = document.toBuilder()
        .id(payload.getAggregateId())
        .ownerId(Identifier.convertToString(payload.getOwnerId()))
        .title(payload.getTitle())
        .content(payload.getContent())
        .tags(payload.getTags())
        .updatedAt(payload.getUpdatedAt())
        .option(payload.getOption());

    if (!payload.getOption().isHasAutoTag()) {
      builder.autoTags(Collections.emptyList());
    }

    setAdditionalProperties(builder, payload.getContent());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "DocumentDeletedEvent")
  public void handle(DocumentDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }

  @ServiceActivator(inputChannel = "AutoTagsUpdatedEvent")
  public void handle(AutoTagsUpdatedEvent payload) {
    DocumentReadModel document = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    if (!document.getOption().isHasAutoTag()) {
      return;
    }

    DocumentReadModel.DocumentReadModelBuilder builder = document.toBuilder()
        .autoTags(payload.getAutoTags());

    repository.save(builder.build());
  }

  private void setAdditionalProperties(DocumentReadModel.DocumentReadModelBuilder builder,
      String content) {
    String replacedContent = content.replaceAll("[\\r\\n]+", " ");
    builder.preview(substringPreview(replacedContent, PREVIEW_LIMIT));
  }

  private String substringPreview(String plainText, int previewLimit) {
    return StringUtils.length(plainText) <= previewLimit
        ? plainText
        : plainText.substring(0, plainText.offsetByCodePoints(0, previewLimit)) + "...";
  }
}
