package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.DifferenceDTO;
import com.closememo.query.controller.client.dto.SimpleDifferenceDTO;
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

  public List<SimpleDifferenceDTO> getDifferencesByDocumentId(String ownerId, String documentId) {
    TypedQuery<SimpleDifferenceDTO> query = em
        .createQuery(
            "SELECT d FROM SimpleDifferenceDTO d"
                + " WHERE d.ownerId = :ownerId AND d.documentId = :documentId"
                + " ORDER BY d.createdAt DESC",
            SimpleDifferenceDTO.class);
    query.setParameter("ownerId", ownerId);
    query.setParameter("documentId", documentId);

    return query.getResultList();
  }

  public DifferenceDTO getDifference(String ownerId, String differenceId) {
    TypedQuery<DifferenceDTO> query = em
        .createQuery(
            "SELECT d FROM DifferenceDTO d"
                + " WHERE d.id = :differenceId AND d.ownerId = :ownerId",
            DifferenceDTO.class);
    query.setParameter("ownerId", ownerId);
    query.setParameter("differenceId", differenceId);

    return query.getSingleResult();
  }
}
