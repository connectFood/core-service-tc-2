package com.connectfood.core.application.restaurantstype.usecase;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class UpdateRestaurantTypeUseCase {

  private final RestaurantTypeGateway repository;
  private final RestaurantsTypeAppMapper mapper;

  public UpdateRestaurantTypeUseCase(RestaurantTypeGateway repository, RestaurantsTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantsTypeOutput execute(final UUID uuid, final RestaurantsTypeInput input) {
    repository.findById(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurant Type Not Found " + uuid));

    final var restaurantsType = repository.update(uuid, mapper.toDomain(uuid, input));

    return mapper.toOutput(restaurantsType);
  }
}
