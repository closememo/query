package com.closememo.query.controller.client.dto;

import com.closememo.query.infra.converter.RolesConverter;
import com.closememo.query.infra.type.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT a.id, a.roles, a.document_count, a.document_order_type FROM accounts a")
@Synchronize({"accounts"})
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
