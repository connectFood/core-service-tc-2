package com.connectfood.core.application.restaurants.useCase;

import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsRepository;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class FindRestaurantsUseCase {

  private final RestaurantsRepository repository;
  private final RestaurantsAppMapper mapper;

  public FindRestaurantsUseCase(RestaurantsRepository repository, RestaurantsAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public RestaurantsOutput execute(final UUID uuid) {
    final var restaurants = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    return mapper.toOutput(restaurants);
  }
}
