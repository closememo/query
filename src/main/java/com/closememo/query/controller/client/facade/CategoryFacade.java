package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.CategoryDAO;
import com.closememo.query.controller.client.dto.CategoryDTO;
import com.closememo.query.controller.system.facade.CategoryNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class CategoryFacade {

  private final CategoryDAO categoryDAO;

  public CategoryFacade(CategoryDAO categoryDAO) {
    this.categoryDAO = categoryDAO;
  }

  public List<CategoryDTO> getCategories(String ownerId) {
    List<CategoryDTO> categories = categoryDAO.getCategories(ownerId);
    Map<String, CategoryDTO> categoryMap = categories.stream()
        .collect(Collectors.toMap(CategoryDTO::getId, Function.identity()));

    CategoryDTO root = categoryMap.values().stream()
        .filter(CategoryDTO::getIsRoot)
        .findFirst()
        .orElseThrow(CategoryNotFoundException::new);

    Map<String, Integer> netCountMap = getNetCountMap(root, categoryMap);

    return categories.stream()
        .map(categoryDTO -> categoryDTO.setNetCount(netCountMap.get(categoryDTO.getId())))
        .collect(Collectors.toList());
  }

  private static Map<String, Integer> getNetCountMap(CategoryDTO target,
      Map<String, CategoryDTO> categoryMap) {

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
