package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantsGateway;
import com.connectfood.core.domain.repository.RestaurantsTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantsUseCase {

  private final RestaurantsGateway repository;
  private final RestaurantsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantsTypeGateway restaurantsTypeGateway;

  public UpdateRestaurantsUseCase(
      final RestaurantsGateway repository,
      final RestaurantsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantsTypeGateway restaurantsTypeGateway
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantsTypeGateway = restaurantsTypeGateway;
  }

  @Transactional
  public RestaurantsOutput execute(final RequestUser requestUser, final UUID uuid, final RestaurantsInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var restaurants = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    RestaurantType restaurantType = restaurants.getRestaurantType();

    if (!restaurants.getRestaurantType()
        .getUuid()
        .equals(input.getRestaurantsTypeUuid())) {
      restaurantType = restaurantsTypeGateway.findById(input.getRestaurantsTypeUuid())
          .orElseThrow(() -> new NotFoundException("Restaurant type not found"));
    }

    final var restaurantsUpdated = repository.update(uuid, mapper.toDomain(uuid, input, restaurantType));

    return mapper.toOutput(restaurantsUpdated);
  }
}
