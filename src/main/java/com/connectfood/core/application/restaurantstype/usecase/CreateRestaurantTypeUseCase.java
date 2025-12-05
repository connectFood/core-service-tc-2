package com.connectfood.core.application.restaurantstype.usecase;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantTypeUseCase {

  private final RestaurantsTypeRepository repository;
  private final RestaurantsTypeAppMapper mapper;

  public CreateRestaurantTypeUseCase(final RestaurantsTypeRepository repository,
      final RestaurantsTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantsTypeOutput execute(final RestaurantsTypeInput input) {
    final var restaurantsType = repository.save(mapper.toDomain(input));

    return mapper.toOutput(restaurantsType);
  }
}
