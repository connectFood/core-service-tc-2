package com.connectfood.core.application.restaurant.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantAppMapper;
import com.connectfood.core.domain.repository.RestaurantGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchRestaurantsUseCase {

  private final RestaurantGateway repository;
  private final RestaurantAppMapper mapper;

  public SearchRestaurantsUseCase(RestaurantGateway repository, RestaurantAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantOutput>> execute(
      final String name,
      final UUID restaurantsTypeUuid,
      final String street,
      final String city,
      final String state,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction
  ) {
    final var restaurants = repository.findAll(name, restaurantsTypeUuid, street, city, state, page, size, sort,
        direction
    );

    final var results = restaurants.content()
        .stream()
        .map(mapper::toOutputAll)
        .toList();

    return new PageOutput<>(results, restaurants.total());
  }
}
