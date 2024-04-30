package com.closememo.query.controller.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT s.id, s.writer_id, s.preview, s.created_at, s.status"
    + " FROM suggestions s WHERE s.status != 'DELETED'")
@Synchronize({"suggestions"})
public class SuggestionListElementDTO {

  @Id
  private String id;
  @JsonIgnore
  private String writerId;
  private String preview;
  private ZonedDateTime createdAt;
  private String status;
}
