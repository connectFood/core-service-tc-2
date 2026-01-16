package com.connectfood.core.application.restauranttype.usecase;

import com.connectfood.core.application.restauranttype.dto.RestaurantTypeInput;
import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.application.restauranttype.mapper.RestaurantTypeAppMapper;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantTypeUseCase {

  private final RestaurantTypeGateway repository;
  private final RestaurantTypeAppMapper mapper;

  public CreateRestaurantTypeUseCase(final RestaurantTypeGateway repository,
      final RestaurantTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantTypeOutput execute(final RestaurantTypeInput input) {
    final var restaurantsType = repository.save(mapper.toDomain(input));

    return mapper.toOutput(restaurantsType);
  }
}
