package com.closememo.query.controller.system.dao;

import com.closememo.query.controller.system.dto.SystemNotificationDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemNotificationDAO {

  private final EntityManager em;

  public SystemNotificationDAO(EntityManager em) {
    this.em = em;
  }

  public Optional<SystemNotificationDTO> getCurrentNotification() {
    ZonedDateTime now = ZonedDateTime.now();
    TypedQuery<SystemNotificationDTO> query = em
        .createQuery(
            "SELECT n FROM SystemNotificationDTO n"
                + " WHERE n.status = 'ACTIVE' AND n.notifyStart < :now AND :now < n.notifyEnd",
            SystemNotificationDTO.class);
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

  public long count() {
    TypedQuery<Long> query = em
        .createQuery("SELECT COUNT(n) FROM SystemNotificationDTO n", Long.class);
    return query.getSingleResult();
  }

  public List<SystemNotificationDTO> getNotifications(int offset, int limit) {
    TypedQuery<SystemNotificationDTO> query = em
        .createQuery(
            "SELECT n FROM SystemNotificationDTO n ORDER BY n.createdAt DESC",
            SystemNotificationDTO.class)
        .setFirstResult(offset)
        .setMaxResults(limit);

    return query.getResultList();
  }

  public SystemNotificationDTO getNotification(String notificationId) {
    TypedQuery<SystemNotificationDTO> query = em
        .createQuery(
            "SELECT n FROM SystemNotificationDTO n WHERE n.id = :notificationId",
            SystemNotificationDTO.class);
    query.setParameter("notificationId", notificationId);

    return query.getSingleResult();
  }
}
