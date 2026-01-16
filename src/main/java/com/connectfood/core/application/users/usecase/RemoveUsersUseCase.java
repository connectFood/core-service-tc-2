package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveUsersUseCase {

  private final UsersGateway repository;

  public RemoveUsersUseCase(final UsersGateway repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users not found"));

    repository.delete(uuid);
  }
}
