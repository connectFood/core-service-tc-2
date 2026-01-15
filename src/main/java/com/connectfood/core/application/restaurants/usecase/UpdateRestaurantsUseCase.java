package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantsUseCase {

  private final RestaurantsRepository repository;
  private final RestaurantsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantsTypeRepository restaurantsTypeRepository;

  public UpdateRestaurantsUseCase(
      final RestaurantsRepository repository,
      final RestaurantsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantsTypeRepository restaurantsTypeRepository
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantsTypeRepository = restaurantsTypeRepository;
  }

  @Transactional
  public RestaurantsOutput execute(final RequestUser requestUser, final UUID uuid, final RestaurantsInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var restaurants = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    RestaurantsType restaurantsType = restaurants.getRestaurantsType();

    if (!restaurants.getRestaurantsType()
        .getUuid()
        .equals(input.getRestaurantsTypeUuid())) {
      restaurantsType = restaurantsTypeRepository.findById(input.getRestaurantsTypeUuid())
          .orElseThrow(() -> new NotFoundException("Restaurant type not found"));
    }

    final var restaurantsUpdated = repository.update(uuid, mapper.toDomain(uuid, input, restaurantsType));

    return mapper.toOutput(restaurantsUpdated);
  }
}
