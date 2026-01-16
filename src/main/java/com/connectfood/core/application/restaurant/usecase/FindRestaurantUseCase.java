package com.connectfood.core.application.restaurant.usecase;

import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantGateway;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class FindRestaurantUseCase {

  private final RestaurantGateway repository;
  private final RestaurantAppMapper mapper;

  public FindRestaurantUseCase(RestaurantGateway repository, RestaurantAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public RestaurantOutput execute(final UUID uuid) {
    final var restaurants = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    return mapper.toOutput(restaurants);
  }
}
