package com.closememo.query.infra.persistence.readmodel.category;

import com.closememo.query.infra.converter.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(
    name = "categories",
    indexes = {
        @Index(name = "idx_owner_id", columnList = "ownerId")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class CategoryReadModel {

  @Id
  @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(24)")
  private String id;
  @Column(nullable = false, columnDefinition = "VARCHAR(24)")
  private String ownerId;
  @Column(nullable = false, columnDefinition = "VARCHAR(150)")
  private String name;
  @Column(nullable = false)
  private ZonedDateTime createdAt;
  private boolean isRoot;
  @Column(columnDefinition = "VARCHAR(24)")
  private String parentId;
  private int depth;
  private int count;
  // ReadOnly
  @Column(columnDefinition = "JSON")
  @Convert(converter = StringListConverter.class)
  private List<String> childrenIds;

  @Builder(toBuilder = true)
  public CategoryReadModel(String id, String ownerId, String name, ZonedDateTime createdAt,
      boolean isRoot, String parentId, int depth, int count, List<String> childrenIds) {
    this.id = id;
    this.ownerId = ownerId;
    this.name = name;
    this.createdAt = createdAt;
    this.isRoot = isRoot;
    this.parentId = parentId;
    this.depth = depth;
    this.count = count;
    this.childrenIds = childrenIds;
  }
}
