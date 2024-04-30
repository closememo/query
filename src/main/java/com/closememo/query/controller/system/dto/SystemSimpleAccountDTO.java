package com.closememo.query.controller.system.dto;

import com.closememo.query.infra.converter.RolesConverter;
import com.closememo.query.infra.type.Role;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@NoArgsConstructor
@Immutable
@Subselect("SELECT a.id, a.email, a.roles FROM accounts a")
@Synchronize({"accounts"})
public class SystemSimpleAccountDTO {

  @Id
  private String id;
  private String email;
  @Convert(converter = RolesConverter.class)
  private Set<Role> roles;
}
