package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersRepository;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class RemoveUsersUseCase {

  private final UsersRepository repository;

  public RemoveUsersUseCase(final UsersRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users not found"));

    repository.delete(uuid);
  }
}
