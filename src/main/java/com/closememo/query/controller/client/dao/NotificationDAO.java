package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.CurrentNotificationDTO;
import java.time.ZonedDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationDAO {

  private final EntityManager em;

  public NotificationDAO(EntityManager em) {
    this.em = em;
  }

  public Optional<CurrentNotificationDTO> getCurrentNotification() {
    ZonedDateTime now = ZonedDateTime.now();
    TypedQuery<CurrentNotificationDTO> query = em
        .createQuery(
            "SELECT n FROM CurrentNotificationDTO n"
                + " WHERE n.notifyStart < :now AND :now < n.notifyEnd",
            CurrentNotificationDTO.class);
    query.setParameter("now", now);

    try {
      return Optional.of(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    } catch (NonUniqueResultException e) {
      log.error("Current notification must be unique.");
      return Optional.empty();
    }
  }
}
