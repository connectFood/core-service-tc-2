package com.connectfood.core.application.restaurants.usecase;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class CreateRestaurantsUseCase {

  private final RestaurantsRepository repository;
  private final RestaurantsAppMapper mapper;
  private final RestaurantsTypeRepository restaurantsTypeRepository;

  public CreateRestaurantsUseCase(
      final RestaurantsRepository repository,
      final RestaurantsAppMapper mapper,
      final RestaurantsTypeRepository restaurantsTypeRepository
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.restaurantsTypeRepository = restaurantsTypeRepository;
  }

  @Transactional
  public RestaurantsOutput execute(final RestaurantsInput input) {
    final var restaurantsType = restaurantsTypeRepository
        .findById(input.getRestaurantsTypeUuid())
        .orElseThrow(() -> new NotFoundException("Restaurants Type not found"));

    final var restaurants = repository.save(mapper.toDomain(input, restaurantsType));

    return mapper.toOutput(restaurants);
  }
}
