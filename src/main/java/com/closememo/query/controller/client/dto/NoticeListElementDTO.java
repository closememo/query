package com.closememo.query.controller.client.dto;

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
@Subselect("SELECT n.id, n.title, n.preview, n.created_at FROM notices n")
@Synchronize({"notices"})
public class NoticeListElementDTO implements Serializable {

  @Id
  private String id;
  private String title;
  private String preview;
  private ZonedDateTime createdAt;
}
