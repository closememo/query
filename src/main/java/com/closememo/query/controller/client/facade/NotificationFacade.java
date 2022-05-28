package com.closememo.query.controller.client.facade;

import com.closememo.query.controller.client.dao.NotificationDAO;
import com.closememo.query.controller.client.dto.CurrentNotificationDTO;
import com.closememo.query.controller.shared.dto.OptionalDTO;
import org.springframework.stereotype.Component;

@Component
public class NotificationFacade {

  private final NotificationDAO notificationDAO;

  public NotificationFacade(NotificationDAO notificationDAO) {
    this.notificationDAO = notificationDAO;
  }

  public OptionalDTO<CurrentNotificationDTO> getCurrentNotification() {
    return notificationDAO.getCurrentNotification()
        .map(OptionalDTO::new)
        .orElseGet(OptionalDTO::empty);
  }
}
