package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.difference.DifferenceCreatedEvent;
import com.closememo.query.infra.messaging.payload.difference.DifferenceDeletedEvent;
import com.closememo.query.infra.messaging.payload.document.AutoTagsUpdatedEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentCategoryUpdatedEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentCreatedEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentDeletedEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentDeletedStatusSetEvent;
import com.closememo.query.infra.messaging.payload.document.DocumentUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModel;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModel.Status;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModelRepository;
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
        .option(payload.getOption())
        .status(payload.getStatus())
        .diffCount(0);
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

  @ServiceActivator(inputChannel = "DocumentCategoryUpdatedEvent")
  public void handle(DocumentCategoryUpdatedEvent payload) {
    DocumentReadModel document = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    DocumentReadModel.DocumentReadModelBuilder builder = document.toBuilder()
        .categoryId(Identifier.convertToString(payload.getCategoryId()));

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "DocumentDeletedEvent")
  public void handle(DocumentDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }

  @ServiceActivator(inputChannel = "DocumentDeletedStatusSetEvent")
  public void handle(DocumentDeletedStatusSetEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(document -> {
          DocumentReadModel.DocumentReadModelBuilder builder = document.toBuilder()
              .status(Status.DELETED);
          repository.save(builder.build());
        });
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

  @ServiceActivator(inputChannel = "DifferenceCreatedEvent")
  public void handle(DifferenceCreatedEvent payload) {
    String documentId = Identifier.convertToString(payload.getDocumentId());
    DocumentReadModel document = repository.findById(documentId)
        .orElseThrow(ResourceNotFoundException::new);

    int before = document.getDiffCount();
    DocumentReadModel.DocumentReadModelBuilder builder = document.toBuilder()
        .diffCount(before + 1);

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "DifferenceDeletedEvent")
  public void handle(DifferenceDeletedEvent payload) {
    String documentId = Identifier.convertToString(payload.getDocumentId());
    DocumentReadModel document = repository.findById(documentId)
        .orElseThrow(ResourceNotFoundException::new);

    int before = document.getDiffCount();
    DocumentReadModel.DocumentReadModelBuilder builder = document.toBuilder()
        .diffCount(before - 1);

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
