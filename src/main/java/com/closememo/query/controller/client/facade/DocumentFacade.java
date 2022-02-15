package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.CategoryDAO;
import com.closememo.query.controller.client.dao.DocumentDAO;
import com.closememo.query.controller.client.dao.DocumentOrderType;
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

  private final CategoryDAO categoryDAO;
  private final DocumentDAO documentDAO;
  private final ElasticsearchClient elasticsearchClient;

  public DocumentFacade(CategoryDAO categoryDAO,
      DocumentDAO documentDAO,
      ElasticsearchClient elasticsearchClient) {
    this.categoryDAO = categoryDAO;
    this.documentDAO = documentDAO;
    this.elasticsearchClient = elasticsearchClient;
  }

  public OffsetPage<SimpleDocumentDTO> getDocuments(String ownerId, String categoryId,
      DocumentOrderType orderType, int page, int limit) {

    if (StringUtils.isBlank(categoryId)) {
      categoryId = categoryDAO.getRootCategoryId(ownerId);
    }

    long total = documentDAO.count(ownerId, categoryId);
    if (total == 0L) {
      return OffsetPage.empty();
    }

    int offset = (page - 1) * limit;
    List<SimpleDocumentDTO> documents = documentDAO.getDocuments(ownerId, categoryId,
        orderType, offset, limit + 1);

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
