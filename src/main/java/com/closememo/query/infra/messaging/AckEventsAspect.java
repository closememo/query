package com.closememo.query.infra.messaging;

import com.closememo.query.infra.messaging.publisher.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AckEventsAspect {

  private final MessagePublisher messagePublisher;

  public AckEventsAspect(
      MessagePublisher messagePublisher) {
    this.messagePublisher = messagePublisher;
  }

  @AfterReturning("execution(* com.closememo.query.infra.messaging.handler..*(*))")
  public void afterReturning(JoinPoint joinPoint) {
    Object payload = joinPoint.getArgs()[0];
    if (payload instanceof DomainEvent && ((DomainEvent) payload).isNeedAck()) {
      DomainEvent domainEvent = (DomainEvent) payload;
      log.debug("publishing ack event. aggregateId=" + domainEvent.aggregateId);
      messagePublisher.publish(new AckEvent(domainEvent.getAggregateId(), domainEvent.getEventVersion()));
    }
  }
}
