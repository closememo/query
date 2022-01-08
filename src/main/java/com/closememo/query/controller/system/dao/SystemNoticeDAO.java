package com.closememo.query.controller.system.dao;

import com.closememo.query.controller.system.dto.SystemNoticeDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;

@Component
public class SystemNoticeDAO {

  private final EntityManager em;

  public SystemNoticeDAO(EntityManager em) {
    this.em = em;
  }

  public long count() {
    TypedQuery<Long> query = em
        .createQuery("SELECT COUNT(n) FROM SystemNoticeDTO n", Long.class);

    return query.getSingleResult();
  }

  public List<SystemNoticeDTO> getNotices(int offset, int limit) {
    TypedQuery<SystemNoticeDTO> query = em
        .createQuery(
            "SELECT n FROM SystemNoticeDTO n ORDER BY n.createdAt DESC",
            SystemNoticeDTO.class)
        .setFirstResult(offset)
        .setMaxResults(limit);

    return query.getResultList();
  }

  public SystemNoticeDTO getNotice(String noticeId) {
    TypedQuery<SystemNoticeDTO> query = em
        .createQuery(
            "SELECT n FROM SystemNoticeDTO n WHERE n.id = :noticeId",
            SystemNoticeDTO.class);
    query.setParameter("noticeId", noticeId);

    return query.getSingleResult();
  }
}
