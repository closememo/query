package com.closememo.query.controller.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Getter
@Immutable
@Subselect("SELECT a.id FROM accounts a")
@Synchronize({"accounts"})
@ToString
public class LoggedInAccountDTO implements Serializable {

  @JsonIgnore
  @Id
  private String id;

  public boolean getIsLoggedIn() {
    return true;
  }
}
