package com.closememo.query.controller.system.facade;

import com.closememo.query.controller.shared.dto.OffsetPage;
import com.closememo.query.controller.system.dao.SystemNoticeDAO;
import com.closememo.query.controller.system.dto.SystemNoticeDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SystemNoticeFacade {

  private final SystemNoticeDAO systemNoticeDAO;

  public SystemNoticeFacade(SystemNoticeDAO systemNoticeDAO) {
    this.systemNoticeDAO = systemNoticeDAO;
  }

  public OffsetPage<SystemNoticeDTO> getNotices(int page, int limit) {
    long total = systemNoticeDAO.count();
    if (total == 0L) {
      return OffsetPage.empty();
    }

    int offset = (page - 1) * limit;
    List<SystemNoticeDTO> notices = systemNoticeDAO.getNotices(offset, limit + 1);

    boolean hasNext = notices.size() > limit;
    List<SystemNoticeDTO> truncated = hasNext ? notices.subList(0, limit) : notices;

    return new OffsetPage<>(truncated, total, page, limit, hasNext);
  }

  public SystemNoticeDTO getNotice(String noticeId) {
    return systemNoticeDAO.getNotice(noticeId);
  }
}
