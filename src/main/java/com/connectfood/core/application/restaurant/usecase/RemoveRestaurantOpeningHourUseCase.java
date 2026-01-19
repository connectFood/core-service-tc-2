package com.connectfood.core.application.restaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantOpeningHourGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveRestaurantOpeningHourUseCase {

  private final RestaurantOpeningHourGateway repository;
  private final RequestUserGuard guard;

  public RemoveRestaurantOpeningHourUseCase(
      final RestaurantOpeningHourGateway repository,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.guard = guard;
  }

  @Transactional
  public void execute(final RequestUser requestUser, final UUID uuid) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant opening hours not found"));

    repository.delete(uuid);
  }
}
