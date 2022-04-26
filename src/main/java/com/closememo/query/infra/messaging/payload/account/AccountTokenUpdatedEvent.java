package com.closememo.query.infra.messaging.payload.account;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModel.Token;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountTokenUpdatedEvent extends DomainEvent {

  private Identifier accountId;
  private List<Token> tokens;
}
