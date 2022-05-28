package com.closememo.query.infra.persistence.readmodel.notification;

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
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class NotificationReadModel {

  @Id
  @Column(unique = true, nullable = false)
  private String id;
  @Column(nullable = false, columnDefinition = "VARCHAR(150)")
  private String title;
  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;
  @Column(nullable = false)
  private ZonedDateTime createdAt;
  private ZonedDateTime notifyStart;
  private ZonedDateTime notifyEnd;
  @Column(nullable = false, columnDefinition = "VARCHAR(20)")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Builder(toBuilder = true)
  public NotificationReadModel(String id, String title, String content,
      ZonedDateTime createdAt, ZonedDateTime notifyStart, ZonedDateTime notifyEnd,
      Status status) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
    this.notifyStart = notifyStart;
    this.notifyEnd = notifyEnd;
    this.status = status;
  }

  public enum Status {
    ACTIVE,
    INACTIVE
  }
}
