package com.closememo.query.infra.messaging.payload.document;

import com.closememo.query.infra.messaging.DomainEvent;
import com.closememo.query.infra.messaging.payload.Identifier;
import com.closememo.query.infra.persistence.readmodel.document.DocumentReadModel.DocumentOption;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentUpdatedEvent extends DomainEvent {

  private Identifier ownerId;
  private String title;
  private String previousContent;
  private String content;
  private List<String> tags;
  private ZonedDateTime updatedAt;
  private DocumentOption previousOption;
  private DocumentOption option;
}
