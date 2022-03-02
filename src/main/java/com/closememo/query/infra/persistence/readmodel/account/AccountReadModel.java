package com.closememo.query.infra.persistence.readmodel.account;

import com.closememo.query.infra.converter.StringRolesConverter;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class AccountReadModel {

  @Id
  @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(24)")
  private String id;
  @Column(nullable = false, columnDefinition = "VARCHAR(100)")
  private String email;
  @ElementCollection
  @CollectionTable(
      name = "tokens",
      joinColumns = @JoinColumn(name = "account_id"),
      indexes = {
          @Index(name = "idx_token_id", columnList = "tokenId", unique = true)
      }
  )
  public List<Token> tokens;
  @Column(nullable = false, columnDefinition = "VARCHAR(100)")
  @Convert(converter = StringRolesConverter.class)
  private Set<String> roles;
  @Embedded
  private AccountOption option;
  @Column(nullable = false)
  private ZonedDateTime createdAt;

  @Builder(toBuilder = true)
  public AccountReadModel(String id, String email, List<Token> tokens,
      Set<String> roles, AccountOption option, ZonedDateTime createdAt) {
    this.id = id;
    this.email = email;
    this.tokens = tokens;
    this.roles = roles;
    this.option = option;
    this.createdAt = createdAt;
  }

  public void clearSecretKeys() {
    this.tokens = Collections.emptyList();
  }

  @Embeddable
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Token {

    @Column(columnDefinition = "VARCHAR(24)")
    private String tokenId;
    private long exp;
    @Column(columnDefinition = "VARCHAR(24)")
    private String childId;
  }

  @Embeddable
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class AccountOption {

    @Column(columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private DocumentOrderType documentOrderType;
    private int documentCount;
  }

  public enum DocumentOrderType {
    CREATED_NEWEST,
    CREATED_OLDEST,
    UPDATED_NEWEST,
  }
}
