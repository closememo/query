package com.closememo.query.controller.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT b.id, d.owner_id, b.document_id, d.title, d.preview, b.bookmark_order"
    + " FROM bookmarks b LEFT JOIN documents d ON b.document_id = d.id"
    + " WHERE d.status = 'NORMAL'")
@Synchronize({"bookmarks", "documents"})
public class BookmarkedDocumentDTO implements Serializable {

  @Id
  private String id;
  @JsonIgnore
  private String ownerId;
  private String documentId;
  private String title;
  private String preview;
  @JsonIgnore
  private int bookmarkOrder;
}
