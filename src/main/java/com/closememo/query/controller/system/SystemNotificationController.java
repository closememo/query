package com.closememo.query.controller.system;

import com.closememo.query.controller.shared.dto.OffsetPage;
import com.closememo.query.controller.system.dto.SystemNotificationDTO;
import com.closememo.query.controller.system.facade.SystemNotificationFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@SystemQueryInterface
public class SystemNotificationController {

  private final SystemNotificationFacade systemNotificationFacade;

  public SystemNotificationController(
      SystemNotificationFacade systemNotificationFacade) {
    this.systemNotificationFacade = systemNotificationFacade;
  }

  @GetMapping("/current-notification")
  public SystemNotificationDTO getCurrentNotification() {
    return systemNotificationFacade.getCurrentNotification();
  }

  @GetMapping("/notifications")
  public OffsetPage<SystemNotificationDTO> getNotifications(
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer limit) {

    return systemNotificationFacade.getNotifications(page, limit);
  }

  @GetMapping("/notifications/{notificationId}")
  public SystemNotificationDTO getNotification(@PathVariable String notificationId) {
    return systemNotificationFacade.getNotification(notificationId);
  }
}
