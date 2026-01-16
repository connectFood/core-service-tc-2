package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantsGateway;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class RemoveRestaurantsUseCase {

  private final RestaurantsGateway repository;
  private final RequestUserGuard guard;

  public RemoveRestaurantsUseCase(
      final RestaurantsGateway repository,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.guard = guard;
  }

  @Transactional
  public void execute(final RequestUser requestUser, final UUID uuid) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    repository.delete(uuid);
  }
}
