package com.closememo.query.controller.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT n.id, n.title, n.content, n.notify_start, n.notify_end"
    + " FROM notifications n WHERE n.status = 'ACTIVE'")
@Synchronize({"notifications"})
public class CurrentNotificationDTO implements Serializable {

  @Id
  private String id;
  private String title;
  private String content;
  @JsonIgnore
  private ZonedDateTime notifyStart;
  @JsonIgnore
  private ZonedDateTime notifyEnd;
}
