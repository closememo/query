package com.closememo.query.controller.system.dao;

import com.closememo.query.controller.system.dto.SystemAccountDTO;
import com.closememo.query.controller.system.dto.SystemSimpleAccountDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.Optional;
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

  public Optional<SystemAccountDTO> selectByToken(@NonNull String token) {
    TypedQuery<SystemAccountDTO> query = em
        .createQuery(
            "SELECT a FROM SystemAccountDTO a WHERE a.tokenId = :token",
            SystemAccountDTO.class);
    query.setParameter("token", token);

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
