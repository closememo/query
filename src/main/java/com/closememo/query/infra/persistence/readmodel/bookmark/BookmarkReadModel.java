package com.closememo.query.infra.persistence.readmodel.bookmark;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
    name = "bookmarks",
    indexes = {
        @Index(name = "idx_owner_id", columnList = "ownerId")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class BookmarkReadModel {

  @Id
  @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(24)")
  private String id;
  @Column(nullable = false, columnDefinition = "VARCHAR(24)")
  private String ownerId;
  @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(24)")
  private String documentId;
  private int bookmarkOrder; // 유일성은 DB 가 아니라 코드에서 책임진다.
  @Column(nullable = false)
  private ZonedDateTime createdAt;

  @Builder(toBuilder = true)
  public BookmarkReadModel(String id, String ownerId, String documentId,
      int bookmarkOrder, ZonedDateTime createdAt) {
    this.id = id;
    this.ownerId = ownerId;
    this.documentId = documentId;
    this.bookmarkOrder = bookmarkOrder;
    this.createdAt = createdAt;
  }
}
