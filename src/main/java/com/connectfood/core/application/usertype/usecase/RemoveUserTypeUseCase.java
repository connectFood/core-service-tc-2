package com.connectfood.core.application.usertype.usecase;

import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveUserTypeUseCase {

  private final UserTypeGateway repository;

  public RemoveUserTypeUseCase(final UserTypeGateway repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users type not found"));

    repository.delete(uuid);
  }
}
