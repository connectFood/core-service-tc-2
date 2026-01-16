package com.connectfood.core.application.restauranttype.usecase;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.application.restauranttype.mapper.RestaurantTypeAppMapper;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SearchRestaurantsTypeUseCase {

  private final RestaurantTypeGateway repository;
  private final RestaurantTypeAppMapper mapper;

  public SearchRestaurantsTypeUseCase(RestaurantTypeGateway repository, RestaurantTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<RestaurantTypeOutput>> execute(final String name, final Integer page, final Integer size,
      final String sort, final String direction) {
    final var restaurantsTypes = repository.findAll(name, page, size, sort, direction);

    final var results = restaurantsTypes.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, restaurantsTypes.total());
  }
}
