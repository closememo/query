package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT d.id, d.owner_id, d.category_id, d.title, d.preview, d.tags, d.auto_tags,"
    + " d.created_at, d.updated_at, b.id AS bookmark_id"
    + " FROM documents d LEFT JOIN bookmarks b ON d.id = b.document_id"
    + " WHERE d.status = 'NORMAL'")
@Synchronize({"documents"})
public class SimpleDocumentDTO implements Serializable {

  @Id
  private String id;
  @JsonIgnore
  private String ownerId;
  @JsonIgnore
  private String categoryId;
  private String title;
  private String preview;
  @Convert(converter = StringListConverter.class)
  private List<String> tags;
  @Convert(converter = StringListConverter.class)
  private List<String> autoTags;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
  @JsonIgnore
  private String bookmarkId;

  public boolean isBookmarked() {
    return bookmarkId != null;
  }
}
