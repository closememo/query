package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.AccountDAO;
import com.closememo.query.controller.client.dto.LoggedInAccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountFacade {

  private final AccountDAO accountDAO;

  public AccountFacade(AccountDAO accountDAO) {
    this.accountDAO = accountDAO;
  }

  public LoggedInAccountDTO getLoggedInAccount(String accountId) {
    return accountDAO.getLoggedInAccount(accountId);
  }
}
