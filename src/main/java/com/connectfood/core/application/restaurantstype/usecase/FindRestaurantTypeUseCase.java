package com.connectfood.core.application.restaurantstype.usecase;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class FindRestaurantTypeUseCase {

  private final RestaurantsTypeGateway repository;
  private final RestaurantsTypeAppMapper mapper;

  public FindRestaurantTypeUseCase(RestaurantsTypeGateway repository, RestaurantsTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public RestaurantsTypeOutput execute(final UUID uuid) {
    final var restaurantsType = repository.findById(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant type Not Found"));

    return mapper.toOutput(restaurantsType);
  }
}
