package com.closememo.query.controller.system;

import com.closememo.query.controller.shared.dto.OffsetPage;
import com.closememo.query.controller.system.dto.SystemSuggestionDTO;
import com.closememo.query.controller.system.facade.SystemSuggestionFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SystemQueryInterface
public class SystemSuggestionController {

  private final SystemSuggestionFacade systemSuggestionFacade;

  public SystemSuggestionController(
      SystemSuggestionFacade systemSuggestionFacade) {
    this.systemSuggestionFacade = systemSuggestionFacade;
  }

  @GetMapping("/suggestions")
  public OffsetPage<SystemSuggestionDTO> getSuggestions(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer limit,
      @RequestParam(required = false) String status) {

    return systemSuggestionFacade.getSuggestions(page, limit, status);
  }
}
