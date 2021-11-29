package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.DifferenceDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class DifferenceDAO {

  private final EntityManager em;

  public DifferenceDAO(EntityManager em) {
    this.em = em;
  }

  public List<DifferenceDTO> getDifferencesByDocumentId(String documentId) {
    TypedQuery<DifferenceDTO> query = em
        .createQuery(
            "SELECT d FROM DifferenceDTO d"
                + " WHERE d.documentId = :documentId ORDER BY d.createdAt DESC",
            DifferenceDTO.class);
    query.setParameter("documentId", documentId);

    return query.getResultList();
  }
}
