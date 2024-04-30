package com.closememo.query.controller.system.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT n.id, n.title, n.content, n.created_at, n.notify_start, n.notify_end, n.status"
    + " FROM notifications n")
@Synchronize({"notifications"})
public class SystemNotificationDTO implements Serializable {

  @Id
  private String id;
  private String title;
  private String content;
  private ZonedDateTime createdAt;
  private ZonedDateTime notifyStart;
  private ZonedDateTime notifyEnd;
  @Enumerated(EnumType.STRING)
  private Status status;
  @Transient
  private Boolean exist = Boolean.TRUE;

  public enum Status {
    ACTIVE,
    INACTIVE
  }

  public void setExist(Boolean exist) {
    this.exist = exist;
  }

  public static SystemNotificationDTO empty() {
    SystemNotificationDTO systemNotificationDTO = new SystemNotificationDTO();
    systemNotificationDTO.setExist(false);
    return systemNotificationDTO;
  }
}
