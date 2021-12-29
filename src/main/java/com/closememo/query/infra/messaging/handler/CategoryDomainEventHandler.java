package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.payload.category.CategoryCountDecreasedEvent;
import com.closememo.query.infra.messaging.payload.category.CategoryCountIncreasedEvent;
import com.closememo.query.infra.messaging.payload.category.CategoryCreatedEvent;
import com.closememo.query.infra.messaging.payload.category.CategoryDeletedEvent;
import com.closememo.query.infra.messaging.payload.category.CategoryUpdatedEvent;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModel;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModelRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CategoryDomainEventHandler {

  private final CategoryReadModelRepository repository;

  public CategoryDomainEventHandler(CategoryReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "CategoryCreatedEvent")
  public void handle(CategoryCreatedEvent payload) {
    String parentId = Identifier.convertToString(payload.getParentId());
    CategoryReadModel parentCategory = repository.findById(parentId)
        .orElseThrow(ResourceNotFoundException::new);

    CategoryReadModel.CategoryReadModelBuilder builder = CategoryReadModel.builder()
        .id(payload.getAggregateId())
        .ownerId(Identifier.convertToString(payload.getOwnerId()))
        .name(payload.getName())
        .createdAt(payload.getCreatedAt())
        .isRoot(payload.getIsRoot())
        .parentId(parentId)
        .depth(payload.getDepth())
        .count(payload.getCount())
        .childrenIds(Collections.emptyList())
        .netCount(0);
    CategoryReadModel savedCategory = repository.save(builder.build());

    // TODO: childrenIds 가 항상 null 이 아니도록 수정
    List<String> childrenIds = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(parentCategory.getChildrenIds())) {
      childrenIds = new ArrayList<>(parentCategory.getChildrenIds());
    }
    childrenIds.add(savedCategory.getId());
    CategoryReadModel.CategoryReadModelBuilder parentBuilder = parentCategory.toBuilder()
        .childrenIds(childrenIds);
    repository.save(parentBuilder.build());
  }

  @ServiceActivator(inputChannel = "CategoryUpdatedEvent")
  public void handle(CategoryUpdatedEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .name(payload.getName());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "CategoryCountIncreasedEvent")
  @Transactional
  public void handle(CategoryCountIncreasedEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);
    // 카테고리 count, netCount 증가
    int count = category.getCount();
    int netCount = category.getNetCount();
    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .count(count + 1)
        .netCount(netCount + 1);
    repository.save(builder.build());

    Map<String, CategoryReadModel> categoryMap = repository.findAllByOwnerId(category.getOwnerId())
        .collect(Collectors.toMap(CategoryReadModel::getId, Function.identity()));
    // 부모의 netCount 증가
    CategoryReadModel cursor = category;
    while (true) {
      CategoryReadModel parent = categoryMap.get(cursor.getParentId());
      if (parent == null) {
        break;
      }
      int parentNetCount = parent.getNetCount();
      CategoryReadModel.CategoryReadModelBuilder parentBuilder = parent.toBuilder()
          .netCount(parentNetCount + 1);
      repository.save(parentBuilder.build());
      cursor = parent;
    }
  }

  @ServiceActivator(inputChannel = "CategoryCountDecreasedEvent")
  @Transactional
  public void handle(CategoryCountDecreasedEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);
    // 카테고리 count, netCount 감소
    int count = category.getCount();
    int netCount = category.getNetCount();
    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .count(count - 1)
        .netCount(netCount - 1);
    repository.save(builder.build());

    Map<String, CategoryReadModel> categoryMap = repository.findAllByOwnerId(category.getOwnerId())
        .collect(Collectors.toMap(CategoryReadModel::getId, Function.identity()));
    // 부모의 netCount 감소
    CategoryReadModel cursor = category;
    while (true) {
      CategoryReadModel parent = categoryMap.get(cursor.getParentId());
      if (parent == null) {
        break;
      }
      int parentNetCount = parent.getNetCount();
      CategoryReadModel.CategoryReadModelBuilder parentBuilder = parent.toBuilder()
          .netCount(parentNetCount - 1);
      repository.save(parentBuilder.build());
      cursor = parent;
    }
  }

  @ServiceActivator(inputChannel = "CategoryDeletedEvent")
  public void handle(CategoryDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }
}
