package com.closememo.query.controller.system.dto;

import com.closememo.query.infra.converter.RolesConverter;
import com.closememo.query.infra.type.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@NoArgsConstructor
@Immutable
@Subselect("SELECT a.id, a.roles, t.token_id FROM accounts a LEFT JOIN tokens t ON a.id = t.account_id")
@Synchronize({"accounts", "tokens"})
public class SystemAccountDTO {

  @Id
  private String id;
  @Convert(converter = RolesConverter.class)
  private Set<Role> roles;
  @JsonIgnore
  private String tokenId;
}
