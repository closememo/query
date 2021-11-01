package com.closememo.query.controller.client;

import com.closememo.query.controller.client.dto.DocumentDTO;
import com.closememo.query.controller.client.dto.SimpleDocumentDTO;
import com.closememo.query.controller.client.facade.DocumentFacade;
import com.closememo.query.config.openapi.apitags.DocumentApiTag;
import com.closememo.query.config.security.authentication.account.AccountId;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@DocumentApiTag
@ClientQueryInterface
public class DocumentController {

  private final DocumentFacade documentFacade;

  public DocumentController(DocumentFacade documentFacade) {
    this.documentFacade = documentFacade;
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/documents")
  public List<SimpleDocumentDTO> getDocuments(@AuthenticationPrincipal AccountId accountId) {
    return documentFacade.getDocuments(accountId.getId());
  }

  @Operation(summary = "get document by document id")
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/documents/{documentId}")
  public DocumentDTO getDocument(@PathVariable String documentId,
      @AuthenticationPrincipal AccountId accountId) {
    return documentFacade.getDocument(documentId,
        Optional.ofNullable(accountId).map(AccountId::getId).orElse(null));
  }
}
