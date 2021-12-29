package com.closememo.query.infra.batch;

import com.closememo.query.infra.exception.ResourceNotFoundException;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModel;
import com.closememo.query.infra.persistence.readmodel.category.CategoryReadModelRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatchService {

  private final CategoryReadModelRepository repository;
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public BatchService(
      CategoryReadModelRepository repository,
      NamedParameterJdbcTemplate jdbcTemplate) {
    this.repository = repository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional
  public List<String> step1() {
    List<Account> accounts = jdbcTemplate.query(
        "SELECT a.id FROM accounts a",
        Map.of(),
        Account.rowMapper());

    return accounts.stream()
        .map(Account::getId)
        .collect(Collectors.toList());
  }

  @Transactional
  public void step2(String accountId) {
    step2_1(accountId);
    step2_2(accountId);
  }

  public void step2_1(String accountId) {
    List<CategoryReadModel> categories = repository.findAllByOwnerId(accountId)
        .collect(Collectors.toList());

    Map<String, List<CategoryReadModel>> parentIdCategoryMap = categories.stream()
        .filter(category -> !category.isRoot())
        .collect(Collectors.groupingBy(CategoryReadModel::getParentId));

    for (CategoryReadModel category : categories) {
      List<String> childrenIds = getChildrenIds(category.getId(), parentIdCategoryMap);
      CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
          .childrenIds(childrenIds);

      repository.save(builder.build());
    }
  }

  public List<String> getChildrenIds(String targetId,
      Map<String, List<CategoryReadModel>> parentIdCategoryMap) {

    List<CategoryReadModel> childrenCategories = parentIdCategoryMap.get(targetId);
    if (CollectionUtils.isEmpty(childrenCategories)) {
      return Collections.emptyList();
    }

    return childrenCategories.stream()
        .map(CategoryReadModel::getId)
        .collect(Collectors.toList());
  }

  private void step2_2(String accountId) {
    List<CategoryReadModel> categories = repository.findAllByOwnerId(accountId)
        .collect(Collectors.toList());

    Map<String, CategoryReadModel> idCategoryMap = categories.stream()
        .collect(Collectors.toMap(CategoryReadModel::getId, Function.identity()));

    CategoryReadModel root = categories.stream()
        .filter(CategoryReadModel::isRoot)
        .findFirst()
        .orElseThrow(ResourceNotFoundException::new);

    Map<String, Integer> idNetCountMap = getIdNetCountMap(root, idCategoryMap);

    categories.forEach(category -> {
      CategoryReadModel.CategoryReadModelBuilder builder = category.toBuilder()
          .netCount(idNetCountMap.get(category.getId()));
      repository.save(builder.build());
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

  @Getter
  private static class Account {

    private final String id;

    public Account(String id) {
      this.id = id;
    }

    private static RowMapper<Account> rowMapper() {
      return (rs, rowNum) -> new Account(rs.getString("id"));
    }
  }
}
