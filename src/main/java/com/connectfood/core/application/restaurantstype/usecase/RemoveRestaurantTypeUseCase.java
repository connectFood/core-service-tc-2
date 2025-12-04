package com.connectfood.core.application.restaurantstype.usecase;

import com.connectfood.core.application.usertype.usecase.RemoveUserTypeUseCase;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class RemoveRestaurantTypeUseCase {

  private final RestaurantsTypeRepository repository;

  public RemoveRestaurantTypeUseCase(final RestaurantsTypeRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findById(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Type not found: " + uuid));

    repository.delete(uuid);
  }

}
