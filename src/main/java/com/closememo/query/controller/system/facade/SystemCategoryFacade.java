package com.closememo.query.controller.system.facade;

import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModel;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModelRepository;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModelRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SystemCategoryFacade {

  private final CategoryReadModelRepository categoryRepository;
  private final DocumentReadModelRepository documentRepository;

  public SystemCategoryFacade(
      CategoryReadModelRepository categoryRepository,
      DocumentReadModelRepository documentRepository) {
    this.categoryRepository = categoryRepository;
    this.documentRepository = documentRepository;
  }

  @Transactional
  public void refreshCategory(String accountId) {
    // 순서가 중요하다.
    refreshChildrenIds(accountId);
    refreshCount(accountId);
    refreshNetCount(accountId);
  }

  private void refreshChildrenIds(String accountId) {
    List<CategoryReadModel> categories = categoryRepository.findAllByOwnerId(accountId)
        .collect(Collectors.toList());

    Map<String, List<CategoryReadModel>> parentIdCategoryMap = categories.stream()
        .filter(category -> !category.isRoot())
        .collect(Collectors.groupingBy(CategoryReadModel::getParentId));

    for (CategoryReadModel category : categories) {
      List<String> childrenIds = getChildrenIds(category.getId(), parentIdCategoryMap);
      CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
          .childrenIds(childrenIds);

      categoryRepository.save(builder.build());
    }
  }

  private static List<String> getChildrenIds(String targetId,
      Map<String, List<CategoryReadModel>> parentIdCategoryMap) {

    List<CategoryReadModel> childrenCategories = parentIdCategoryMap.get(targetId);
    if (CollectionUtils.isEmpty(childrenCategories)) {
      return Collections.emptyList();
    }

    return childrenCategories.stream()
        .map(CategoryReadModel::getId)
        .collect(Collectors.toList());
  }

  private void refreshCount(String accountId) {
    List<CategoryReadModel> categories = categoryRepository.findAllByOwnerId(accountId)
        .collect(Collectors.toList());

    for (CategoryReadModel category : categories) {
      long count = documentRepository.countByCategoryId(category.getId());
      CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
          .count((int) count);

      categoryRepository.save(builder.build());
    }
  }

  private void refreshNetCount(String accountId) {
    List<CategoryReadModel> categories = categoryRepository.findAllByOwnerId(accountId)
        .collect(Collectors.toList());

    Map<String, CategoryReadModel> idCategoryMap = categories.stream()
        .collect(Collectors.toMap(CategoryReadModel::getId, Function.identity()));

    CategoryReadModel root = categories.stream()
        .filter(CategoryReadModel::isRoot)
        .findFirst()
        .orElseThrow(CategoryNotFoundException::new);

    Map<String, Integer> idNetCountMap = getIdNetCountMap(root, idCategoryMap);

    categories.forEach(category -> {
      CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
          .netCount(idNetCountMap.get(category.getId()));
      categoryRepository.save(builder.build());
    });
  }

  private Map<String, Integer> getIdNetCountMap(CategoryReadModel target,
      Map<String, CategoryReadModel> idCategoryMap) {

    List<String> childrenIds = target.getChildrenIds();

    if (CollectionUtils.isEmpty(childrenIds)) {
      return Map.of(target.getId(), target.getCount());
    }

    Map<String, Integer> idNetCountMap = childrenIds.stream()
        .map(idCategoryMap::get)
        .map(childCategory -> getIdNetCountMap(childCategory, idCategoryMap))
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

    int childrenNetCountSum = idNetCountMap.entrySet().stream()
        .filter(entry -> childrenIds.contains(entry.getKey()))
        .mapToInt(Entry::getValue)
        .sum();
    int netCount = target.getCount() + childrenNetCountSum;
    idNetCountMap.put(target.getId(), netCount);

    return idNetCountMap;
  }
}
