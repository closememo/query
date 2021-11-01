package com.closememo.query.config.security.authentication.account;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class AccountId {

  private String id;

  public AccountId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return id;
  }
}
