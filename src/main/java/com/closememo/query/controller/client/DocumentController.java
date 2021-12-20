package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.DocumentApiTag;
import com.closememo.query.config.security.authentication.account.AccountId;
import com.closememo.query.controller.client.dto.DocumentDTO;
import com.closememo.query.controller.client.dto.SimpleDocumentDTO;
import com.closememo.query.controller.client.facade.DocumentFacade;
import com.closememo.query.controller.shared.dto.OffsetPage;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@DocumentApiTag
@ClientQueryInterface
public class DocumentController {

  private final DocumentFacade documentFacade;

  public DocumentController(DocumentFacade documentFacade) {
    this.documentFacade = documentFacade;
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/documents")
  public OffsetPage<SimpleDocumentDTO> getDocuments(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "20") Integer limit,
      @AuthenticationPrincipal AccountId accountId) {

    return documentFacade.getDocuments(accountId.getId(), page, limit);
  }

  @Operation(summary = "get document by document id")
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/documents/{documentId}")
  public DocumentDTO getDocument(@PathVariable String documentId,
      @AuthenticationPrincipal AccountId accountId) {
    return documentFacade.getDocument(documentId,
        Optional.ofNullable(accountId).map(AccountId::getId).orElse(null));
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/documents-by-tag")
  public List<SimpleDocumentDTO> getDocumentsByTag(@RequestParam String tag,
      @AuthenticationPrincipal AccountId accountId) {
    return documentFacade.getDocumentsByTag(accountId.getId(), tag);
  }
}
