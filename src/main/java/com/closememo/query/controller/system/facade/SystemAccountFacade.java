package com.closememo.query.controller.system.facade;

import com.closememo.query.controller.system.dao.SystemAccountDAO;
import com.closememo.query.controller.system.dto.SystemAccountDTO;
import com.closememo.query.controller.system.dto.SystemSimpleAccountDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SystemAccountFacade {

  private final SystemAccountDAO systemAccountDAO;

  public SystemAccountFacade(SystemAccountDAO systemAccountDAO) {
    this.systemAccountDAO = systemAccountDAO;
  }

  public SystemAccountDTO getAccountById(@NonNull String id) {
    if (StringUtils.isBlank(id)) {
      throw new AccountNotFoundException();
    }

    return systemAccountDAO.select(id)
        .orElseThrow(AccountNotFoundException::new);
  }

  public SystemAccountDTO getAccountByToken(@NonNull String accessToken) {
    return systemAccountDAO.selectByToken(accessToken)
        .orElseThrow(AccountNotFoundException::new);
  }

  public SystemSimpleAccountDTO getAccountByEmail(String email) {
    if (StringUtils.isBlank(email)) {
      throw new AccountNotFoundException();
    }

    return systemAccountDAO.selectByEmail(email)
        .orElseThrow(AccountNotFoundException::new);
  }
}
