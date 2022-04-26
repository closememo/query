package com.closememo.query.infra.messaging.payload.difference;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.persistence.readmodel.difference.DifferenceReadModel.LineDelta;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DifferenceCreatedEvent extends DomainEvent {

  private Identifier differenceId;
  private Identifier documentId;
  private long documentVersion;
  private List<LineDelta> lineDeltas;
  private ZonedDateTime createdAt;
}
