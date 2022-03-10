package com.closememo.query.infra.messaging.payload.account;

import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModel.AccountOption;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModel.AccountTrack;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModel.Token;
import com.closememo.query.infra.messaging.DomainEvent;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountCreatedEvent extends DomainEvent {

  private Identifier accountId;
  private String email;
  private List<Token> tokens;
  private Set<String> roles;
  private AccountOption option;
  private AccountTrack track;
  private ZonedDateTime createdAt;
}
