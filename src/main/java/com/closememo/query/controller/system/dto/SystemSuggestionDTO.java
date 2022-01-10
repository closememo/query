package com.closememo.query.controller.system.dto;

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
@Subselect("SELECT s.id, s.writer_id, a.email, s.content, s.created_at, s.status"
    + " FROM suggestions s LEFT JOIN accounts a ON s.writer_id = a.id")
@Synchronize({"suggestions", "accounts"})
public class SystemSuggestionDTO {

  @Id
  private String id;
  private String writerId;
  private String email;
  private String content;
  private ZonedDateTime createdAt;
  private String status;
}
