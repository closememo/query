package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.AccountApiTag;
import com.closememo.query.config.security.authentication.account.AccountId;
import com.closememo.query.controller.client.dto.LoggedInAccountDTO;
import com.closememo.query.controller.client.facade.AccountFacade;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@AccountApiTag
@ClientQueryInterface
public class AccountController {

  private final AccountFacade accountFacade;

  public AccountController(AccountFacade accountFacade) {
    this.accountFacade = accountFacade;
  }

  @Operation(summary = "Get logged in account")
  @PreAuthorize("hasRole('USER')")
  @GetMapping("/accounts/me")
  public LoggedInAccountDTO me(@AuthenticationPrincipal AccountId accountId) {
    return accountFacade.getLoggedInAccount(accountId.getId());
  }
}
