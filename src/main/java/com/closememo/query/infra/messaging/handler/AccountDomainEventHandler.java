package com.closememo.query.infra.messaging.handler;

import com.closememo.query.infra.messaging.payload.account.AccountCreatedEvent;
import com.closememo.query.infra.messaging.payload.account.AccountDeletedEvent;
import com.closememo.query.infra.messaging.payload.account.AccountTokenUpdatedEvent;
import com.closememo.query.infra.messaging.payload.account.AccountTokensClearedEvent;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModel;
import com.closememo.query.infra.persistence.readmodel.account.AccountReadModelRepository;
import com.closememo.query.infra.exception.ResourceNotFoundException;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class AccountDomainEventHandler {

  private final AccountReadModelRepository repository;

  public AccountDomainEventHandler(
      AccountReadModelRepository repository) {
    this.repository = repository;
  }

  @ServiceActivator(inputChannel = "AccountCreatedEvent")
  public void handle(AccountCreatedEvent payload) {
    AccountReadModel account = new AccountReadModel(payload.getAggregateId(),
        payload.getEmail(), payload.getTokens(), payload.getRoles(), payload.getCreatedAt());

    repository.save(account);
  }

  @ServiceActivator(inputChannel = "AccountTokenUpdatedEvent")
  public void handle(AccountTokenUpdatedEvent payload) {
    AccountReadModel account = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    AccountReadModel.AccountReadModelBuilder builder = account.toBuilder()
        .tokens(payload.getTokens());

    repository.save(builder.build());
  }

  @ServiceActivator(inputChannel = "AccountTokensClearedEvent")
  public void handle(AccountTokensClearedEvent payload) {
    AccountReadModel account = repository.findById(payload.getAggregateId())
        .orElseThrow(ResourceNotFoundException::new);

    account.clearSecretKeys();

    repository.save(account);
  }

  @ServiceActivator(inputChannel = "AccountDeletedEvent")
  public void handle(AccountDeletedEvent payload) {
    repository.findById(payload.getAggregateId())
        .ifPresent(repository::delete);
  }
}
