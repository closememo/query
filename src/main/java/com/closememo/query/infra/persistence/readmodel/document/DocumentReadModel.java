package com.closememo.query.infra.persistence.readmodel.document;

import com.closememo.query.infra.converter.StringListConverter;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class DocumentReadModel {

  @Id
  @Column(unique = true, nullable = false)
  private String id;
  @Column(nullable = false, columnDefinition = "VARCHAR(24)")
  private String ownerId;
  @Column(columnDefinition = "VARCHAR(24)") // TODO: 이후 nullable = false 처리
  private String categoryId;
  @Column(columnDefinition = "VARCHAR(150)")
  private String title;
  @Column(nullable = false, columnDefinition = "LONGTEXT")
  private String content;
  @Column(columnDefinition = "JSON")
  @Convert(converter = StringListConverter.class)
  private List<String> tags;
  @Column(columnDefinition = "JSON")
  @Convert(converter = StringListConverter.class)
  private List<String> autoTags;
  @Column(nullable = false)
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;
  @Embedded
  private DocumentOption option;

  // ReadOnly
  private String preview;

  @Builder(toBuilder = true)
  public DocumentReadModel(String id, String ownerId, String categoryId, String title,
      String content, List<String> tags, List<String> autoTags, ZonedDateTime createdAt,
      ZonedDateTime updatedAt, String preview, DocumentOption option) {

    this.id = id;
    this.ownerId = ownerId;
    this.categoryId = categoryId;
    this.title = title;
    this.content = content;
    this.tags = tags;
    this.autoTags = autoTags;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.preview = preview;
    this.option = option;
  }

  @Embeddable
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class DocumentOption {

    private boolean hasAutoTag;
  }
}
