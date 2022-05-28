package com.closememo.query.controller.system.facade;

import com.closememo.query.controller.shared.dto.OffsetPage;
import com.closememo.query.controller.system.dao.SystemNotificationDAO;
import com.closememo.query.controller.system.dto.SystemNotificationDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SystemNotificationFacade {

  private final SystemNotificationDAO systemNotificationDAO;

  public SystemNotificationFacade(
      SystemNotificationDAO systemNotificationDAO) {
    this.systemNotificationDAO = systemNotificationDAO;
  }

  public SystemNotificationDTO getCurrentNotification() {
    return systemNotificationDAO.getCurrentNotification()
        .orElse(SystemNotificationDTO.empty());
  }

  public OffsetPage<SystemNotificationDTO> getNotifications(int page, int limit) {
    long total = systemNotificationDAO.count();
    if (total == 0L) {
      return OffsetPage.empty();
    }

    int offset = (page - 1) * limit;
    List<SystemNotificationDTO> notifications = systemNotificationDAO.getNotifications(offset, limit + 1);

    boolean hasNext = notifications.size() > limit;
    List<SystemNotificationDTO> truncated = hasNext ? notifications.subList(0, limit) : notifications;

    return new OffsetPage<>(truncated, total, page, limit, hasNext);
  }

  public SystemNotificationDTO getNotification(String notificationId) {
    return systemNotificationDAO.getNotification(notificationId);
  }
}
