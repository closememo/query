package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.DocumentDTO;
import com.closememo.query.controller.client.dto.SimpleDocumentDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class DocumentDAO {

  private final EntityManager em;

  public DocumentDAO(EntityManager em) {
    this.em = em;
  }

  public List<SimpleDocumentDTO> getDocuments(String ownerId) {
    TypedQuery<SimpleDocumentDTO> query = em
        .createQuery(
            "SELECT d FROM SimpleDocumentDTO d WHERE d.ownerId = :ownerId",
            SimpleDocumentDTO.class);
    query.setParameter("ownerId", ownerId);

    return query.getResultList();
  }

  public DocumentDTO getDocument(String documentId) {
    TypedQuery<DocumentDTO> query = em
        .createQuery(
            "SELECT d FROM DocumentDTO d WHERE d.id = :documentId",
            DocumentDTO.class);
    query.setParameter("documentId", documentId);

    return query.getSingleResult();
  }
}
