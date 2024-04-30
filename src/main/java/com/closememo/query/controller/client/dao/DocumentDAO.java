package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.BookmarkedDocumentDTO;
import com.closememo.query.controller.client.dto.DocumentDTO;
import com.closememo.query.controller.client.dto.SimpleDocumentDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DocumentDAO {

  private final EntityManager em;

  public DocumentDAO(EntityManager em) {
    this.em = em;
  }

  public long count(String ownerId, String categoryId) {
    TypedQuery<Long> query = em
        .createQuery(
            "SELECT COUNT(d) FROM SimpleDocumentDTO d"
                + " WHERE d.ownerId = :ownerId AND d.categoryId = :categoryId",
            Long.class);
    query.setParameter("ownerId", ownerId);
    query.setParameter("categoryId", categoryId);

    return query.getSingleResult();
  }

  public List<SimpleDocumentDTO> getDocuments(String ownerId, String categoryId,
      DocumentOrderType orderType, int offset, int limit) {

    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
    CriteriaQuery<SimpleDocumentDTO> criteriaQuery = criteriaBuilder
        .createQuery(SimpleDocumentDTO.class);

    Root<SimpleDocumentDTO> document = criteriaQuery.from(SimpleDocumentDTO.class);
    criteriaQuery.select(document);
    criteriaQuery.where(
        criteriaBuilder.equal(document.get("ownerId"), ownerId),
        criteriaBuilder.equal(document.get("categoryId"), categoryId));

    switch (orderType) {
      case CREATED_NEWEST:
        criteriaQuery.orderBy(criteriaBuilder.desc(document.get("createdAt")));
        break;
      case CREATED_OLDEST:
        criteriaQuery.orderBy(criteriaBuilder.asc(document.get("createdAt")));
        break;
      case UPDATED_NEWEST:
        criteriaQuery.orderBy(
            criteriaBuilder.desc(document.get("updatedAt")),
            criteriaBuilder.desc(document.get("createdAt")));
        break;
      default:
        log.warn("orderType must exist.");
        criteriaQuery.orderBy(criteriaBuilder.desc(document.get("createdAt")));
    }

    TypedQuery<SimpleDocumentDTO> query = em.createQuery(criteriaQuery)
        .setFirstResult(offset)
        .setMaxResults(limit);

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

  public List<BookmarkedDocumentDTO> getBookmarkedDocuments(String ownerId) {
    TypedQuery<BookmarkedDocumentDTO> query = em
        .createQuery(
            "SELECT d FROM BookmarkedDocumentDTO d"
                + " WHERE d.ownerId = :ownerId ORDER BY d.bookmarkOrder",
            BookmarkedDocumentDTO.class);
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
