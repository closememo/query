package com.closememo.query.infra.persistence.readmodel.suggestion;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "suggestions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class SuggestionReadModel {

  @Id
  @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(24)")
  private String id;
  @Column(nullable = false, columnDefinition = "VARCHAR(24)")
  private String writerId;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
  @Column(nullable = false)
  private ZonedDateTime createdAt;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Status status;

  // ReadOnly
  private String preview;

  @Builder(toBuilder = true)
  public SuggestionReadModel(String id, String writerId, String content,
      ZonedDateTime createdAt,
      Status status, String preview) {
    this.id = id;
    this.writerId = writerId;
    this.content = content;
    this.createdAt = createdAt;
    this.status = status;
    this.preview = preview;
  }

  public enum Status {
    REGISTERED,
    CHECKED,
    COMPLETED,
    DELETED
  }
}
