package com.connectfood.core.application.restaurants.useCase;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RemoveRestaurantsUseCase {

  private final RestaurantsRepository repository;

  public RemoveRestaurantsUseCase(RestaurantsRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    repository.delete(uuid);
  }
}
