package com.closememo.query.infra.messaging.payload.account;

import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.DomainEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountTokensClearedEvent extends DomainEvent {

  private Identifier accountId;
}
