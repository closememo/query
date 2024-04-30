package com.closememo.query.controller.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Subselect("SELECT d.id, d.owner_id, d.document_id, d.document_version, d.created_at, d.inserted, d.deleted, d.changed"
    + " FROM differences d")
@Synchronize({"differences"})
public class SimpleDifferenceDTO implements Serializable {

  @Id
  private String id;
  @JsonIgnore
  private String ownerId;
  @JsonIgnore
  private String documentId;
  private long documentVersion;
  private ZonedDateTime createdAt;
  private int inserted;
  private int deleted;
  private int changed;
}
