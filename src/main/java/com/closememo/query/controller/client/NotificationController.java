package com.closememo.query.controller.client;

import com.closememo.query.config.openapi.apitags.NotificationApiTag;
import com.closememo.query.controller.client.dto.CurrentNotificationDTO;
import com.closememo.query.controller.client.facade.NotificationFacade;
import com.closememo.query.controller.shared.dto.OptionalDTO;
import org.springframework.web.bind.annotation.GetMapping;

@NotificationApiTag
@ClientQueryInterface
public class NotificationController {

  private final NotificationFacade notificationFacade;

  public NotificationController(
      NotificationFacade notificationFacade) {
    this.notificationFacade = notificationFacade;
  }

  @GetMapping("/current-notification")
  public OptionalDTO<CurrentNotificationDTO> getCurrentNotification() {
    return notificationFacade.getCurrentNotification();
  }
}
