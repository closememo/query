package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.DifferenceApiTag;
import com.closememo.query.config.security.authentication.account.AccountId;
import com.closememo.query.controller.client.dto.DifferenceDTO;
import com.closememo.query.controller.client.dto.SimpleDifferenceDTO;
import com.closememo.query.controller.client.facade.DifferenceFacade;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@DifferenceApiTag
@ClientQueryInterface
public class DifferenceController {

  private final DifferenceFacade differenceFacade;

  public DifferenceController(DifferenceFacade differenceFacade) {
    this.differenceFacade = differenceFacade;
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/differences-by-document-id")
  public List<SimpleDifferenceDTO> getDifferencesByDocumentId(@RequestParam String documentId,
      @AuthenticationPrincipal AccountId accountId) {

    return differenceFacade.getDifferencesByDocumentId(accountId.getId(), documentId);
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/differences/{differenceId}")
  public DifferenceDTO getDifference(@PathVariable String differenceId,
      @AuthenticationPrincipal AccountId accountId) {

    return differenceFacade.getDifference(accountId.getId(), differenceId);
  }
}
