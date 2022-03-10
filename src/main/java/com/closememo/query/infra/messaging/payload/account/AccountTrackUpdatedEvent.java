package com.closememo.query.infra.messaging.payload.account;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModel.AccountTrack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountTrackUpdatedEvent extends DomainEvent {

  private AccountTrack track;
}
