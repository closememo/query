package com.closememo.query.infra.messaging.payload.notice;

import com.closememo.query.infra.messaging.DomainEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDeletedEvent extends DomainEvent {

}
