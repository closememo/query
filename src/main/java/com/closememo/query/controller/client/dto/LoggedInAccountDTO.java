package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.RolesConverter;
import com.closememo.query.infra.type.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT a.id, a.roles, a.document_count, a.document_order_type, c.id AS recently_viewed_category_id"
    + " FROM accounts a LEFT JOIN categories c ON (a.id = c.owner_id AND a.recently_viewed_category_id = c.id)")
@Synchronize({"accounts", "categories"})
@ToString
public class LoggedInAccountDTO implements Serializable {

  @JsonIgnore
  @Id
  private String id;
  @JsonIgnore
  @Convert(converter = RolesConverter.class)
  private Set<Role> roles;
  @Enumerated(EnumType.STRING)
  private DocumentOrderType documentOrderType;
  private int documentCount;
  private String recentlyViewedCategoryId;

  public boolean getIsLoggedIn() {
    return true;
  }

  public boolean getIsTempUser() {
    return roles.contains(Role.TEMP);
  }

  public enum DocumentOrderType {
    CREATED_NEWEST,
    CREATED_OLDEST,
    UPDATED_NEWEST,
  }
}
