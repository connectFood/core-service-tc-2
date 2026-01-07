package com.connectfood.core.application.usersrestaurant.usecase;

import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersRestaurantRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveUsersRestaurantUseCase {

  private final UsersRestaurantRepository repository;

  public RemoveUsersRestaurantUseCase(final UsersRestaurantRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID usersUuid) {

    final var usersRestaurant =
        repository.findByUsersUuid(usersUuid)
            .orElseThrow(() -> new NotFoundException("Users restaurant not found"));

    repository.delete(usersRestaurant.getUuid());
  }
}
