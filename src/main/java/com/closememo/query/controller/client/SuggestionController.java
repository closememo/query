package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.SuggestionApiTag;
import com.closememo.query.config.security.authentication.account.AccountId;
import com.closememo.query.controller.client.dto.SuggestionDTO;
import com.closememo.query.controller.client.dto.SuggestionListElementDTO;
import com.closememo.query.controller.client.facade.SuggestionFacade;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@SuggestionApiTag
@ClientQueryInterface
public class SuggestionController {

  private final SuggestionFacade suggestionFacade;

  public SuggestionController(
      SuggestionFacade suggestionFacade) {
    this.suggestionFacade = suggestionFacade;
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/suggestions")
  public List<SuggestionListElementDTO> getSuggestions(
      @AuthenticationPrincipal AccountId accountId) {

    return suggestionFacade.getSuggestionListElements(accountId.getId());
  }

  @PreAuthorize("hasRole('USER')")
  @GetMapping("/suggestions/{suggestionId}")
  public SuggestionDTO getSuggestion(
      @PathVariable String suggestionId, @AuthenticationPrincipal AccountId accountId) {

    return suggestionFacade.getSuggestion(suggestionId, accountId.getId());
  }
}
