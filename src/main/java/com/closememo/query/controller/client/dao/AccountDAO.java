package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.LoggedInAccountDTO;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class AccountDAO {

  private final EntityManager em;

  public AccountDAO(EntityManager em) {
    this.em = em;
  }

  public LoggedInAccountDTO getLoggedInAccount(String accountId) {
    TypedQuery<LoggedInAccountDTO> query = em
        .createQuery(
            "SELECT a FROM LoggedInAccountDTO a WHERE a.id = :accountId",
            LoggedInAccountDTO.class);
    query.setParameter("accountId", accountId);

    return query.getSingleResult();
  }
}
