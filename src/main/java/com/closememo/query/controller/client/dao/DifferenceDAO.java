package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.DifferenceDTO;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class DifferenceDAO {

  private final EntityManager em;

  public DifferenceDAO(EntityManager em) {
    this.em = em;
  }

  public DifferenceDTO getDifference(String differenceId) {
    TypedQuery<DifferenceDTO> query = em
        .createQuery(
            "SELECT d FROM DifferenceDTO d WHERE d.id = :differenceId",
            DifferenceDTO.class);
    query.setParameter("differenceId", differenceId);

    return query.getSingleResult();
  }
}
