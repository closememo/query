package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.notice.NoticeCreatedEvent;
import com.closememo.query.infra.messaging.payload.notice.NoticeDeletedEvent;
import com.closememo.query.infra.messaging.payload.notice.NoticeUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.notice.NoticeReadModel;
import com.closememo.query.infra.persistence.readmodel.notice.NoticeReadModelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class NoticeDomainEventHandler {

  private static final int PREVIEW_LIMIT = 150;

  private final NoticeReadModelRepository repository;

  public NoticeDomainEventHandler(
      NoticeReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "NoticeCreatedEvent")
  public void handle(NoticeCreatedEvent payload) {
    NoticeReadModel.NoticeReadModelBuilder builder = NoticeReadModel.builder()
        .id(payload.getAggregateId())
        .title(payload.getTitle())
        .content(payload.getContent())
        .createdAt(payload.getCreatedAt());
    setAdditionalProperties(builder, payload.getContent());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "NoticeUpdatedEvent")
  public void handle(NoticeUpdatedEvent payload) {
    NoticeReadModel notice = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    NoticeReadModel.NoticeReadModelBuilder builder = notice.toBuilder()
        .title(payload.getTitle())
        .content(payload.getContent())
        .updatedAt(payload.getUpdatedAt());

    setAdditionalProperties(builder, payload.getContent());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "NoticeDeletedEvent")
  public void handle(NoticeDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }

  private static void setAdditionalProperties(NoticeReadModel.NoticeReadModelBuilder builder,
      String content) {
    String replacedContent = content.replaceAll("[\\r\\n]+", " ");
    builder.preview(substringPreview(replacedContent, PREVIEW_LIMIT));
  }

  private static String substringPreview(String plainText, int previewLimit) {
    return StringUtils.length(plainText) <= previewLimit
        ? plainText
        : plainText.substring(0, plainText.offsetByCodePoints(0, previewLimit)) + "...";
  }
}
