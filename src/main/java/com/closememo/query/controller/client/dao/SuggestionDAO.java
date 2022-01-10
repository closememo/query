package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.SuggestionDTO;
import com.closememo.query.controller.client.dto.SuggestionListElementDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class SuggestionDAO {

  private final EntityManager em;

  public SuggestionDAO(EntityManager em) {
    this.em = em;
  }

  public List<SuggestionListElementDTO> getSuggestionListElements(String writerId) {
    TypedQuery<SuggestionListElementDTO> query = em
        .createQuery(
            "SELECT s FROM SuggestionListElementDTO s"
                + " WHERE s.writerId = :writerId ORDER BY s.createdAt DESC",
            SuggestionListElementDTO.class);
    query.setParameter("writerId", writerId);

    return query.getResultList();
  }

  public SuggestionDTO getSuggestion(String suggestionId) {
    TypedQuery<SuggestionDTO> query = em
        .createQuery(
            "SELECT s FROM SuggestionDTO s WHERE s.id = :suggestionId",
            SuggestionDTO.class);
    query.setParameter("suggestionId", suggestionId);

    return query.getSingleResult();
  }
}
