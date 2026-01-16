package com.connectfood.core.application.restauranttype.usecase;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class RemoveRestaurantTypeUseCase {

  private final RestaurantTypeGateway repository;

  public RemoveRestaurantTypeUseCase(final RestaurantTypeGateway repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findById(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Type not found: " + uuid));

    repository.delete(uuid);
  }

}
