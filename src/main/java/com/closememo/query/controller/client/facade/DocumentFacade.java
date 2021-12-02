package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.DocumentDAO;
import com.closememo.query.controller.client.dto.DocumentDTO;
import com.closememo.query.controller.client.dto.SimpleDocumentDTO;
import com.closememo.query.controller.shared.dto.OffsetPage;
import com.closememo.query.infra.elasticsearch.ElasticsearchClient;
import com.closememo.query.infra.elasticsearch.request.SearchPostByTagRequest;
import com.closememo.query.infra.exception.AccessDeniedException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DocumentFacade {

  private final DocumentDAO documentDAO;
  private final ElasticsearchClient elasticsearchClient;

  public DocumentFacade(DocumentDAO documentDAO,
      ElasticsearchClient elasticsearchClient) {
    this.documentDAO = documentDAO;
    this.elasticsearchClient = elasticsearchClient;
  }

  // TODO: 제거할 것.
  public List<SimpleDocumentDTO> getDocuments(String ownerId) {
    return documentDAO.getDocuments(ownerId);
  }

  public OffsetPage<SimpleDocumentDTO> getDocuments(String ownerId, int page, int limit) {

    long total = documentDAO.count(ownerId);
    if (total == 0L) {
      return OffsetPage.empty();
    }

    int offset = (page - 1) * limit;
    List<SimpleDocumentDTO> documents = documentDAO.getDocuments(ownerId, offset, limit + 1);

    boolean hasNext = documents.size() > limit;
    List<SimpleDocumentDTO> truncatedDocuments = hasNext ? documents.subList(0, limit) : documents;

    return new OffsetPage<>(truncatedDocuments, total, page, limit, hasNext);
  }

  public DocumentDTO getDocument(String documentId, String ownerId) {
    DocumentDTO documentDTO = documentDAO.getDocument(documentId);
    checkAuthority(documentDTO, ownerId);
    return documentDTO;
  }

  public List<SimpleDocumentDTO> getDocumentsByTag(String ownerId, String tag) {
    SearchPostByTagRequest request = new SearchPostByTagRequest(ownerId, tag);
    List<String> postId = elasticsearchClient.searchPostIdsByTag(request);

    return documentDAO.getDocuments(ownerId, postId);
  }

  private void checkAuthority(DocumentDTO documentDTO, String ownerId) {
    if (!StringUtils.equals(documentDTO.getOwnerId(), ownerId)) {
      throw new AccessDeniedException();
    }
  }
}
