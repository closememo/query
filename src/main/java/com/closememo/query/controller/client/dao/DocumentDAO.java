package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.DocumentDTO;
import com.closememo.query.controller.client.dto.SimpleDocumentDTO;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class DocumentDAO {

  private final EntityManager em;

  public DocumentDAO(EntityManager em) {
    this.em = em;
  }

  public long count(String ownerId) {
    TypedQuery<Long> query = em
        .createQuery(
            "SELECT COUNT(d) FROM SimpleDocumentDTO d WHERE d.ownerId = :ownerId",
            Long.class);
    query.setParameter("ownerId", ownerId);

    return query.getSingleResult();
  }

  // TODO: 제거할 것.
  public List<SimpleDocumentDTO> getDocuments(String ownerId) {
    TypedQuery<SimpleDocumentDTO> query = em
        .createQuery(
            "SELECT d FROM SimpleDocumentDTO d"
                + " WHERE d.ownerId = :ownerId ORDER BY d.createdAt DESC",
            SimpleDocumentDTO.class);
    query.setParameter("ownerId", ownerId);

    return query.getResultList();
  }

  public List<SimpleDocumentDTO> getDocuments(String ownerId, int offset, int limit) {
    TypedQuery<SimpleDocumentDTO> query = em
        .createQuery(
            "SELECT d FROM SimpleDocumentDTO d"
                + " WHERE d.ownerId = :ownerId ORDER BY d.createdAt DESC",
            SimpleDocumentDTO.class)
        .setFirstResult(offset)
        .setMaxResults(limit);
    query.setParameter("ownerId", ownerId);

    return query.getResultList();
  }

  public List<SimpleDocumentDTO> getDocuments(String ownerId, List<String> documentIds) {
    TypedQuery<SimpleDocumentDTO> query = em
        .createQuery(
            "SELECT d FROM SimpleDocumentDTO d"
                + " WHERE d.ownerId = :ownerId AND d.id IN :documentIds ORDER BY d.createdAt DESC",
            SimpleDocumentDTO.class);
    query.setParameter("ownerId", ownerId);
    query.setParameter("documentIds", documentIds);

    Map<String, SimpleDocumentDTO> resultMap = query.getResultList().stream()
        .collect(Collectors.toMap(SimpleDocumentDTO::getId, Function.identity()));

    return documentIds.stream()
        .map(resultMap::get)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
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
