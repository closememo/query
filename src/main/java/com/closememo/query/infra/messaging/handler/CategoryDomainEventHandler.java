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
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
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

    // 이름으로 정렬하여 parent 의 childrenIds 에 추가
    refreshParentChildrenIds(parentCategory, savedCategory);
  }

  @ServiceActivator(inputChannel = "CategoryUpdatedEvent")
  @Transactional
  public void handle(CategoryUpdatedEvent payload) {
    CategoryReadModel category = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .name(payload.getName());

    CategoryReadModel savedCategory = repository.save(builder.build());

    CategoryReadModel parentCategory = repository.findById(category.getParentId())
        .orElseThrow(ResourceNotFoundException::new);

    // 이름으로 정렬하여 parent 의 childrenIds 에 추가
    refreshParentChildrenIds(parentCategory, savedCategory);
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
    adjustCounts(payload.getAggregateId(), PLUS_ONE);
  }

  @ServiceActivator(inputChannel = "CategoryCountDecreasedEvent")
  @Transactional
  public void handle(CategoryCountDecreasedEvent payload) {
    adjustCounts(payload.getAggregateId(), MINUS_ONE);
  }

  private void adjustCounts(String categoryId, int i) {
    CategoryReadModel category = repository.findById(categoryId)
        .orElseThrow(ResourceNotFoundException::new);
    // 카테고리 count, netCount 감소
    int count = category.getCount();
    int netCount = category.getNetCount();
    CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
        .count(count + i)
        .netCount(netCount + i);
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
          .netCount(parentNetCount + i);
      repository.save(parentBuilder.build());
      cursor = parent;
    }
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

    // netCount 재계산
    recalculateNetCount(categoryMap);
  }

  private void recalculateNetCount(Map<String, CategoryReadModel> categoryMap) {
    CategoryReadModel root = categoryMap.values().stream()
        .filter(CategoryReadModel::isRoot)
        .findFirst()
        .orElseThrow(ResourceNotFoundException::new);

    Map<String, Integer> netCountMap = getNetCountMap(root, categoryMap);

    categoryMap.values().stream()
        .filter(category -> Objects.nonNull(netCountMap.get(category.getId())))
        .forEach(category -> {
          CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
              .netCount(netCountMap.get(category.getId()));
          repository.save(builder.build());
        });
  }

  private static Map<String, Integer> getNetCountMap(CategoryReadModel target,
      Map<String, CategoryReadModel> categoryMap) {

    List<String> childrenIds = target.getChildrenIds();

    if (CollectionUtils.isEmpty(childrenIds)) {
      return Map.of(target.getId(), target.getCount());
    }

    Map<String, Integer> netCountMap = childrenIds.stream()
        .map(categoryMap::get)
        .map(childCategory -> getNetCountMap(childCategory, categoryMap))
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    int childrenNetCountSum = netCountMap.entrySet().stream()
        .filter(entry -> childrenIds.contains(entry.getKey()))
        .mapToInt(Entry::getValue)
        .sum();
    int netCount = target.getCount() + childrenNetCountSum;
    netCountMap.put(target.getId(), netCount);

    return netCountMap;
  }
}
