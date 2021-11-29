package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.DifferenceApiTag;
import com.closememo.query.controller.client.dto.DifferenceDTO;
import com.closememo.query.controller.client.facade.DifferenceFacade;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
  public List<DifferenceDTO> getDifferencesByDocumentId(@RequestParam String documentId) {
    return differenceFacade.getDifferencesByDocumentId(documentId);
  }
}
