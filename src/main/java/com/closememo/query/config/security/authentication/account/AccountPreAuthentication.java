package com.closememo.query.config.security.authentication.account;

import java.util.Set;
import lombok.Getter;

@Getter
public class AccountPreAuthentication {

  private final AccountId accountId;
  private final Set<String> roles;

  public AccountPreAuthentication(AccountId accountId, Set<String> roles) {
    this.accountId = accountId;
    this.roles = roles;
  }
}
