package com.closememo.query.infra.messaging.payload.account;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModel.AccountOption;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountOptionUpdatedEvent extends DomainEvent {

  private AccountOption option;
}
