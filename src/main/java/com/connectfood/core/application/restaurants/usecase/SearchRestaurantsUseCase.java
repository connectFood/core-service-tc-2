package com.connectfood.core.application.restaurants.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.repository.RestaurantGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchRestaurantsUseCase {

  private final RestaurantGateway repository;
  private final RestaurantsAppMapper mapper;

  public SearchRestaurantsUseCase(RestaurantGateway repository, RestaurantsAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantsOutput>> execute(
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
