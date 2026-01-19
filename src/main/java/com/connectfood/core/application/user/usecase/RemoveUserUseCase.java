package com.connectfood.core.application.user.usecase;

import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveUserUseCase {

  private final UserGateway repository;

  public RemoveUserUseCase(final UserGateway repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users not found"));

    repository.delete(uuid);
  }
}
