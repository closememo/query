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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CategoryDomainEventHandler {

  private static final int PLUS_ONE = 1;
  private static final int MINUS_ONE = -1;

  private final CategoryReadModelRepository repository;

  public CategoryDomainEventHandler(CategoryReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "CategoryCreatedEvent")
  @Transactional
  public void handle(CategoryCreatedEvent payload) {
    CategoryReadModel.CategoryReadModelBuilder builder = CategoryReadModel.builder()
        .id(payload.getAggregateId())
        .ownerId(Identifier.convertToString(payload.getOwnerId()))
        .name(payload.getName())
        .createdAt(payload.getCreatedAt())
        .isRoot(payload.getIsRoot())
        .parentId(Identifier.convertToString(payload.getParentId()))
        .depth(payload.getDepth())
        .count(payload.getCount())
        .childrenIds(Collections.emptyList());
    CategoryReadModel savedCategory = repository.save(builder.build());

    if (StringUtils.isNotBlank(savedCategory.getParentId())) {
      repository.findById(savedCategory.getParentId()).ifPresent(parentCategory -> {
        // 이름으로 정렬하여 parent 의 childrenIds 에 추가
        refreshParentChildrenIds(parentCategory, savedCategory);
      });
    }
  }

  @ServiceActivator(inputChannel = "CategoryUpdatedEvent")
  @Transactional
  public void handle(CategoryUpdatedEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .name(payload.getName());

    CategoryReadModel savedCategory = repository.save(builder.build());

    if (StringUtils.isNotBlank(savedCategory.getParentId())) {
      repository.findById(savedCategory.getParentId()).ifPresent(parentCategory -> {
        // 이름으로 정렬하여 parent 의 childrenIds 에 추가
        refreshParentChildrenIds(parentCategory, savedCategory);
      });
    }
  }

  private void refreshParentChildrenIds(CategoryReadModel parentCategory, CategoryReadModel savedCategory) {
    List<String> childrenIds = Stream
        .concat(repository.findAllByIdIn(parentCategory.getChildrenIds()), Stream.of(savedCategory))
        .sorted(Comparator.comparing(CategoryReadModel::getName))
        .map(CategoryReadModel::getId)
        .collect(Collectors.toList());

    CategoryReadModel.CategoryReadModelBuilder parentBuilder = parentCategory.toBuilder()
        .childrenIds(childrenIds);
    repository.save(parentBuilder.build());
  }

  @ServiceActivator(inputChannel = "CategoryCountIncreasedEvent")
  @Transactional
  public void handle(CategoryCountIncreasedEvent payload) {
    changeCounts(payload.getAggregateId(), PLUS_ONE);
  }

  @ServiceActivator(inputChannel = "CategoryCountDecreasedEvent")
  @Transactional
  public void handle(CategoryCountDecreasedEvent payload) {
    changeCounts(payload.getAggregateId(), MINUS_ONE);
  }

  private void changeCounts(String categoryId, int i) {
    CategoryReadModel category = repository.findById(categoryId)
        .orElseThrow(ResourceNotFoundException::new);
    // 카테고리 count 증가/감소
    int count = category.getCount();
    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .count(count + i);
    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "CategoryDeletedEvent")
  @Transactional
  public void handle(CategoryDeletedEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);
    // category 삭제
    repository.delete(category);

    Map<String, CategoryReadModel> categoryMap = repository.findAllByOwnerId(category.getOwnerId())
        .collect(Collectors.toMap(CategoryReadModel::getId, Function.identity()));

    CategoryReadModel parentCategory = categoryMap.get(category.getParentId());
    // 부모 category 가 먼저 삭제된 경우
    if (parentCategory == null) {
      return;
    }

    // parent category 의 childrenIds 수정
    List<String> childrenIds = parentCategory.getChildrenIds().stream()
        .filter(childrenId -> !StringUtils.equals(childrenId, category.getId()))
        .collect(Collectors.toList());
    repository.save(parentCategory.toBuilder().childrenIds(childrenIds).build());
  }
}
