package com.closememo.query.controller.system.dao;

import com.closememo.query.controller.system.dto.SystemSuggestionDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SystemSuggestionDAO {

  private final EntityManager em;

  public SystemSuggestionDAO(EntityManager em) {
    this.em = em;
  }

  public long count() {
    TypedQuery<Long> query = em
        .createQuery("SELECT COUNT(s) FROM SystemSuggestionDTO s", Long.class);

    return query.getSingleResult();
  }

  public List<SystemSuggestionDTO> getSuggestions(int offset, int limit, String status) {
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaQuery<SystemSuggestionDTO> criteriaQuery =
        criteriaBuilder.createQuery(SystemSuggestionDTO.class);

    Root<SystemSuggestionDTO> suggestion = criteriaQuery.from(SystemSuggestionDTO.class);
    criteriaQuery.select(suggestion)
        .orderBy(criteriaBuilder.desc(suggestion.get("createdAt")));

    if (StringUtils.isNotBlank(status)) {
      criteriaQuery.where(criteriaBuilder.equal(suggestion.get("status"), status));
    }

    TypedQuery<SystemSuggestionDTO> query = em.createQuery(criteriaQuery)
        .setFirstResult(offset)
        .setMaxResults(limit);

    return query.getResultList();
  }
}
