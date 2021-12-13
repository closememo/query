package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.CategoryDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class CategoryDAO {

  private final EntityManager em;

  public CategoryDAO(EntityManager em) {
    this.em = em;
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
