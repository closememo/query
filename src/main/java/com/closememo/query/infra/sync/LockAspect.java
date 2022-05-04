package com.closememo.query.infra.sync;

import com.closememo.query.infra.messaging.DomainEvent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * multi-thread 환경에서 동기화 처리를 위해 DomainEvent 의 aggregateId 를 key 로 여기서 Lock 을 걸고 푼다.
 */
@Aspect
@Order(0) // @Transactional 보다 먼저 시작되어야 한다.
@Component
public class LockAspect {

  private final LockManager lockManager;

  public LockAspect(LockManager lockManager) {
    this.lockManager = lockManager;
  }

  @Around("execution(* com.closememo.query.infra.messaging.handler..*(..))" +
      " && @annotation(org.springframework.integration.annotation.ServiceActivator)")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {

    Object payload = pjp.getArgs()[0];
    if (payload instanceof DomainEvent) {
      DomainEvent domainEvent = (DomainEvent) payload;
      Integer hash = domainEvent.getAggregateId().hashCode();

      Object result;
      lockManager.lock(hash);
      try {
        result = pjp.proceed();
      } finally {
        lockManager.unlock(hash);
      }

      return result;
    } else {
      return pjp.proceed();
    }
  }
}
