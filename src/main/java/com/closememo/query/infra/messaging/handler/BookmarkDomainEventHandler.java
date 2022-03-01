package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.bookmark.BookmarkCreatedEvent;
import com.closememo.query.infra.messaging.payload.bookmark.BookmarkDeletedEvent;
import com.closememo.query.infra.messaging.payload.bookmark.BookmarkUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.bookmark.BookmarkReadModel;
import com.closememo.query.infra.persistence.readmodel.bookmark.BookmarkReadModelRepository;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookmarkDomainEventHandler {

  private final BookmarkReadModelRepository repository;

  public BookmarkDomainEventHandler(
      BookmarkReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "BookmarkCreatedEvent")
  @Transactional
  public void handle(BookmarkCreatedEvent payload) {
    BookmarkReadModel.BookmarkReadModelBuilder builder = BookmarkReadModel.builder()
        .id(payload.getAggregateId())
        .ownerId(Identifier.convertToString(payload.getOwnerId()))
        .documentId(Identifier.convertToString(payload.getDocumentId()))
        .bookmarkOrder(payload.getBookmarkOrder())
        .createdAt(payload.getCreatedAt());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "BookmarkUpdatedEvent")
  @Transactional
  public void handle(BookmarkUpdatedEvent payload) {
    BookmarkReadModel bookmark = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    BookmarkReadModel.BookmarkReadModelBuilder builder = bookmark.toBuilder()
        .bookmarkOrder(payload.getBookmarkOrder());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "BookmarkDeletedEvent")
  @Transactional
  public void handle(BookmarkDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }
}
