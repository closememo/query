package com.closememo.query.controller.system.dao;

import com.closememo.query.controller.system.dto.SystemAccountDTO;
import com.closememo.query.controller.system.dto.SystemSimpleAccountDTO;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class SystemAccountDAO {

  private final EntityManager em;

  public SystemAccountDAO(EntityManager em) {
    this.em = em;
  }

  public Optional<SystemAccountDTO> select(@NonNull String id) {
    TypedQuery<SystemAccountDTO> query = em
        .createQuery(
            "SELECT a FROM SystemAccountDTO a WHERE a.id = :id",
            SystemAccountDTO.class);
    query.setParameter("id", id);

    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public Optional<SystemAccountDTO> selectByTokens(@NonNull List<String> tokens) {
    TypedQuery<SystemAccountDTO> query = em
        .createQuery(
            "SELECT a FROM SystemAccountDTO a WHERE a.tokenId IN :tokens",
            SystemAccountDTO.class);
    query.setParameter("tokens", tokens);

    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public Optional<SystemSimpleAccountDTO> selectByEmail(String email) {
    TypedQuery<SystemSimpleAccountDTO> query = em
        .createQuery(
            "SELECT a FROM SystemSimpleAccountDTO a WHERE a.email = :email",
            SystemSimpleAccountDTO.class);
    query.setParameter("email", email);

    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
