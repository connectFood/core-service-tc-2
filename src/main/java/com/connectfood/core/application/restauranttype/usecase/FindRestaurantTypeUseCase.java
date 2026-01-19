package com.connectfood.core.application.restauranttype.usecase;

import java.util.UUID;

import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.application.restauranttype.mapper.RestaurantTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindRestaurantTypeUseCase {

  private final RestaurantTypeGateway repository;
  private final RestaurantTypeAppMapper mapper;

  public FindRestaurantTypeUseCase(RestaurantTypeGateway repository, RestaurantTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public RestaurantTypeOutput execute(final UUID uuid) {
    final var restaurantsType = repository.findById(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant type Not Found"));

    return mapper.toOutput(restaurantsType);
  }
}
