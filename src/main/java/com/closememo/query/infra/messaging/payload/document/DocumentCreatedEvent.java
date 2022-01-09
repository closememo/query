package com.closememo.query.infra.messaging.payload.document;

import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModel.DocumentOption;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModel.Status;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentCreatedEvent extends DomainEvent {

  private Identifier ownerId;
  private Identifier categoryId;
  private String title;
  private String content;
  private List<String> tags;
  private ZonedDateTime createdAt;
  private DocumentOption option;
  private Status status;
}
