package com.connectfood.core.application.restaurantitem.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveRestaurantItemUseCase {

  private final RestaurantItemGateway repository;
  private final RequestUserGuard guard;

  public RemoveRestaurantItemUseCase(
      final RestaurantItemGateway repository,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.guard = guard;
  }

  @Transactional
  public void execute(final RequestUser requestUser, final UUID uuid) {
    guard.requireRole(requestUser, "OWNER");

    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    repository.delete(uuid);
  }
}
