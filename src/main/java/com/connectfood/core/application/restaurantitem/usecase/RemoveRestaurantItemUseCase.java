package com.connectfood.core.application.restaurantitem.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.core.domain.repository.RestaurantItemImageGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveRestaurantItemUseCase {

  private final RestaurantItemGateway repository;
  private final RequestUserGuard guard;
  private final RestaurantItemImageGateway restaurantItemImageGateway;

  public RemoveRestaurantItemUseCase(
      final RestaurantItemGateway repository,
      final RequestUserGuard guard,
      final RestaurantItemImageGateway restaurantItemImageGateway
  ) {
    this.repository = repository;
    this.guard = guard;
    this.restaurantItemImageGateway = restaurantItemImageGateway;
  }

  @Transactional
  public void execute(final RequestUser requestUser, final UUID uuid) {
    guard.requireRole(requestUser, "OWNER");

    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Items not found"));

    final var existsImages = restaurantItemImageGateway.existsByRestaurantItemUuid(uuid);

    if (existsImages) {
      restaurantItemImageGateway.deleteByRestaurantItemUuid(uuid);
    }

    repository.delete(uuid);
  }
}
