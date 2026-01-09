package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveRestaurantOpeningHoursUseCase {

  private final RestaurantOpeningHoursRepository repository;

  public RemoveRestaurantOpeningHoursUseCase(final RestaurantOpeningHoursRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void execute(final UUID uuid) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant opening hours not found"));

    repository.delete(uuid);
  }
}
