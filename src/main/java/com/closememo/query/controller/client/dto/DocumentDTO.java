package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.StringListConverter;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT d.id, d.owner_id, d.title, d.content, d.tags, d.created_at, d.updated_at FROM documents d")
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
}
