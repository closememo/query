package com.closememo.query.controller.client;

import com.closememo.query.controller.client.dto.DifferenceDTO;
import com.closememo.query.controller.client.facade.DifferenceFacade;
import com.closememo.query.config.openapi.apitags.DifferenceApiTag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@DifferenceApiTag
@ClientQueryInterface
public class DifferenceController {

  private final DifferenceFacade differenceFacade;

  public DifferenceController(DifferenceFacade differenceFacade) {
    this.differenceFacade = differenceFacade;
  }

  @GetMapping("/differences/{differenceId}")
  public DifferenceDTO getDifference(@PathVariable String differenceId) {
    return differenceFacade.getDifference(differenceId);
  }
}
