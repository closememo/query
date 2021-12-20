package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.category.CategoryCreatedEvent;
import com.closememo.query.infra.messaging.payload.category.CategoryDeletedEvent;
import com.closememo.query.infra.messaging.payload.category.CategoryUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModel;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModelRepository;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class CategoryDomainEventHandler {

  private final CategoryReadModelRepository repository;

  public CategoryDomainEventHandler(CategoryReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "CategoryCreatedEvent")
  public void handle(CategoryCreatedEvent payload) {
    CategoryReadModel.CategoryReadModelBuilder builder = CategoryReadModel.builder()
        .id(payload.getAggregateId())
        .ownerId(Identifier.convertToString(payload.getOwnerId()))
        .name(payload.getName())
        .createdAt(payload.getCreatedAt())
        .isRoot(payload.getIsRoot())
        .parentId(Identifier.convertToString(payload.getParentId()));

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "CategoryUpdatedEvent")
  public void handle(CategoryUpdatedEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .name(payload.getName());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "CategoryDeletedEvent")
  public void handle(CategoryDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }
}
