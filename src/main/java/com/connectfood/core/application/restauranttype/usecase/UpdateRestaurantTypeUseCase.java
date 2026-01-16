package com.connectfood.core.application.restauranttype.usecase;

import com.connectfood.core.application.restauranttype.dto.RestaurantTypeInput;
import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.application.restauranttype.mapper.RestaurantTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class UpdateRestaurantTypeUseCase {

  private final RestaurantTypeGateway repository;
  private final RestaurantTypeAppMapper mapper;

  public UpdateRestaurantTypeUseCase(RestaurantTypeGateway repository, RestaurantTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantTypeOutput execute(final UUID uuid, final RestaurantTypeInput input) {
    repository.findById(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Type Not Found " + uuid));

    final var restaurantsType = repository.update(uuid, mapper.toDomain(uuid, input));

    return mapper.toOutput(restaurantsType);
  }
}
