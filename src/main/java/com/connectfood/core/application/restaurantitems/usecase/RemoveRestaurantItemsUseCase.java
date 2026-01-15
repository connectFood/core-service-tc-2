package com.connectfood.core.application.restaurantitems.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveRestaurantItemsUseCase {

  private final RestaurantItemsRepository repository;
  private final RequestUserGuard guard;

  public RemoveRestaurantItemsUseCase(
      final RestaurantItemsRepository repository,
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
