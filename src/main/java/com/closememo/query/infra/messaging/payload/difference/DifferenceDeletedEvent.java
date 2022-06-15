package com.closememo.query.infra.messaging.payload.difference;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DifferenceDeletedEvent extends DomainEvent {

  private Identifier differenceId;
  private Identifier documentId;

  /**
   * Difference 삭제에 대한 이벤트이지만, Document 에 대해 lock 을 걸어야 해서 추가
   */
  @Override
  public Integer getHash() {
    return Identifier.convertToString(documentId).hashCode();
  }
}
