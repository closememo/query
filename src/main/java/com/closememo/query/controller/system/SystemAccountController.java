package com.closememo.query.controller.system;

import com.closememo.query.controller.system.dto.SystemAccountDTO;
import com.closememo.query.controller.system.dto.SystemSimpleAccountDTO;
import com.closememo.query.controller.system.facade.SystemAccountFacade;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@SystemQueryInterface
public class SystemAccountController {

  private final SystemAccountFacade systemAccountFacade;

  public SystemAccountController(SystemAccountFacade systemAccountFacade) {
    this.systemAccountFacade = systemAccountFacade;
  }

  @GetMapping("/accounts/{id}")
  public SystemAccountDTO getAccountById(@PathVariable String id) {
    return systemAccountFacade.getAccountById(id);
  }

  @GetMapping("/account-by-tokens")
  public SystemAccountDTO getAccountByTokens(@RequestParam String accessToken,
      @RequestParam(defaultValue = "") String syncToken) {

    return systemAccountFacade.getAccountByTokens(accessToken, syncToken);
  }

  @GetMapping("/account-by-email")
  public SystemSimpleAccountDTO getAccountByEmail(@RequestParam String email) {
    return systemAccountFacade.getAccountByEmail(email);
  }
}
