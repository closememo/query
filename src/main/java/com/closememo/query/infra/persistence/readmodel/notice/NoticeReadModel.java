package com.closememo.query.infra.persistence.readmodel.notice;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class NoticeReadModel {

  @Id
  @Column(unique = true, nullable = false)
  private String id;
  @Column(nullable = false, columnDefinition = "VARCHAR(150)")
  private String title;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
  @Column(nullable = false)
  private ZonedDateTime createdAt;
  private ZonedDateTime updatedAt;

  // ReadOnly
  private String preview;

  @Builder(toBuilder = true)
  public NoticeReadModel(String id, String title, String content, ZonedDateTime createdAt,
      ZonedDateTime updatedAt, String preview) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.preview = preview;
  }
}
