package com.closememo.query.controller.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT s.id, s.writer_id, s.content"
    + " FROM suggestions s WHERE s.status != 'DELETED'")
@Synchronize({"suggestions"})
public class SuggestionDTO {

  @Id
  private String id;
  @JsonIgnore
  private String writerId;
  private String content;
}
