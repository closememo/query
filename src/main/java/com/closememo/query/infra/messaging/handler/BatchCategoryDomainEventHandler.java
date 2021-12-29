package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.category.BatchCategoryDepthAndCountSetEvent;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModel;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModelRepository;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BatchCategoryDomainEventHandler {

  private final CategoryReadModelRepository repository;

  public BatchCategoryDomainEventHandler(CategoryReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "BatchCategoryDepthAndCountSetEvent")
  @Transactional
  public void handle(BatchCategoryDepthAndCountSetEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .depth(payload.getDepth())
        .count(payload.getCount());

    repository.save(builder.build());
  }
}
