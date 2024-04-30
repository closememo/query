package com.closememo.query.controller.client.dao;

import com.closememo.query.controller.client.dto.NoticeDTO;
import com.closememo.query.controller.client.dto.NoticeListElementDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NoticeDAO {

  private final EntityManager em;

  public NoticeDAO(EntityManager em) {
    this.em = em;
  }

  public long count() {
    TypedQuery<Long> query = em
        .createQuery("SELECT COUNT(n) FROM NoticeListElementDTO n", Long.class);

    return query.getSingleResult();
  }

  public List<NoticeListElementDTO> getNoticeListElements(int offset, int limit) {
    TypedQuery<NoticeListElementDTO> query = em
        .createQuery(
            "SELECT n FROM NoticeListElementDTO n ORDER BY n.createdAt DESC",
            NoticeListElementDTO.class)
        .setFirstResult(offset)
        .setMaxResults(limit);

    return query.getResultList();
  }

  public NoticeDTO getNotice(String noticeId) {
    TypedQuery<NoticeDTO> query = em
        .createQuery(
            "SELECT n FROM NoticeDTO n WHERE n.id = :noticeId",
            NoticeDTO.class);
    query.setParameter("noticeId", noticeId);

    return query.getSingleResult();
  }
}
