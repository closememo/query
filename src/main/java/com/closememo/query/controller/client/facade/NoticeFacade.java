package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.NoticeDAO;
import com.closememo.query.controller.client.dto.NoticeDTO;
import com.closememo.query.controller.client.dto.NoticeListElementDTO;
import com.closememo.query.controller.shared.dto.OffsetPage;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class NoticeFacade {

  private final NoticeDAO noticeDAO;

  public NoticeFacade(NoticeDAO noticeDAO) {
    this.noticeDAO = noticeDAO;
  }

  public OffsetPage<NoticeListElementDTO> getNoticeListElements(int page, int limit) {
    long total = noticeDAO.count();
    if (total == 0L) {
      return OffsetPage.empty();
    }

    int offset = (page - 1) * limit;
    List<NoticeListElementDTO> notices = noticeDAO.getNoticeListElements(offset, limit + 1);

    boolean hasNext = notices.size() > limit;
    List<NoticeListElementDTO> truncated = hasNext ? notices.subList(0, limit) : notices;

    return new OffsetPage<>(truncated, total, page, limit, hasNext);
  }

  public NoticeDTO getNotice(String noticeId) {
    return noticeDAO.getNotice(noticeId);
  }
}
