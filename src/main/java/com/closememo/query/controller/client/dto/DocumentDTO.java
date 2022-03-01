package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect(
    "SELECT d.id, d.owner_id, d.title, d.content, d.tags, d.created_at, d.updated_at,"
        + " d.has_auto_tag, b.id AS bookmark_id"
        + " FROM documents d LEFT JOIN bookmarks b ON d.id = b.document_id"
        + " WHERE d.status = 'NORMAL'")
@Synchronize({"documents"})
public class DocumentDTO implements Serializable {

  @Id
  private String id;
  private String ownerId;
  private String title;
  private String content;
  @Convert(converter = StringListConverter.class)
  private List<String> tags;
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
  @Embedded
  private DocumentOption option;
  @JsonIgnore
  private String bookmarkId;

  @Schema(name = "DocumentDTO_DocumentOption")
  @Embeddable
  @Getter
  public static class DocumentOption implements Serializable {

    private boolean hasAutoTag;
  }

  public boolean isBookmarked() {
    return bookmarkId != null;
  }
}
