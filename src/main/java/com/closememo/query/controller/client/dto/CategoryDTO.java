package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT c.id, c.owner_id, c.name, c.created_at, c.is_root, c.parent_id, c.children_ids, c.count, c.depth"
    + " FROM categories c")
@Synchronize({"categories"})
public class CategoryDTO implements Serializable {

  @Id
  private String id;
  @JsonIgnore
  private String ownerId;
  private String name;
  private ZonedDateTime createdAt;
  private Boolean isRoot;
  private String parentId; // TODO: 반영 후 제거
  @Convert(converter = StringListConverter.class)
  private List<String> childrenIds;
  private Integer count;
  private Integer depth;
  @Transient
  private Integer netCount;

  public CategoryDTO setNetCount(int netCount) {
    this.netCount = netCount;
    return this;
  }
}
