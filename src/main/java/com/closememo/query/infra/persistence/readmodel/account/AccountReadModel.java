package com.closememo.query.infra.persistence.readmodel.account;

import com.closememo.query.infra.converter.StringRolesConverter;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
  @Embedded
  private AccountTrack track;
  @Column(nullable = false)
  private ZonedDateTime createdAt;

  @Builder(toBuilder = true)
  public AccountReadModel(String id, String email, List<Token> tokens, Set<String> roles,
      AccountOption option, AccountTrack track, ZonedDateTime createdAt) {
    this.id = id;
    this.email = email;
    this.tokens = tokens;
    this.roles = roles;
    this.option = option;
    this.track = track;
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

  @Embeddable
  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class AccountTrack {

    @Column(columnDefinition = "VARCHAR(24)")
    private String recentlyViewedCategoryId;
  }
}
