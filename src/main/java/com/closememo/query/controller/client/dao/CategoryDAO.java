package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.CategoryDTO;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CategoryDAO {

  private final EntityManager em;
  private final NamedParameterJdbcTemplate jdbcTemplate;

  public CategoryDAO(EntityManager em,
      NamedParameterJdbcTemplate jdbcTemplate) {
    this.em = em;
    this.jdbcTemplate = jdbcTemplate;
  }

  public String getRootCategoryId(String ownerId) {
    return jdbcTemplate.queryForObject(
        "SELECT id FROM categories WHERE owner_id = :ownerId AND is_root = true",
        Map.of("ownerId", ownerId),
        (rs, rowNum) -> rs.getString("id"));
  }

  public List<CategoryDTO> getCategories(String ownerId) {
    TypedQuery<CategoryDTO> query = em
        .createQuery(
            "SELECT c FROM CategoryDTO c WHERE c.ownerId = :ownerId ORDER BY c.createdAt DESC",
            CategoryDTO.class);
    query.setParameter("ownerId", ownerId);

    return query.getResultList();
  }
}
