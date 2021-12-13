package com.closememo.query.controller.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT c.id, c.owner_id, c.name, c.created_at FROM categories c")
@Synchronize({"categories"})
public class CategoryDTO implements Serializable {

  @Id
  private String id;
  @JsonIgnore
  private String ownerId;
  private String name;
  private ZonedDateTime createdAt;
}
