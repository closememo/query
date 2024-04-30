package com.closememo.query.controller.system.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT n.id, n.title, n.content, n.created_at, n.updated_at FROM notices n")
@Synchronize({"notices"})
public class SystemNoticeDTO implements Serializable {

  @Id
  private String id;
  private String title;
  private String content;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
}
