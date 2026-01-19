package com.connectfood.core.application.restaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateRestaurantUseCase {

  private final RestaurantGateway repository;
  private final RestaurantAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantTypeGateway restaurantTypeGateway;

  public UpdateRestaurantUseCase(
      final RestaurantGateway repository,
      final RestaurantAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantTypeGateway restaurantTypeGateway
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantTypeGateway = restaurantTypeGateway;
  }

  @Transactional
  public RestaurantOutput execute(final RequestUser requestUser, final UUID uuid, final RestaurantInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var restaurants = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    RestaurantType restaurantType = restaurants.getRestaurantType();

    if (!restaurants.getRestaurantType()
        .getUuid()
        .equals(input.getRestaurantsTypeUuid())) {
      restaurantType = restaurantTypeGateway.findById(input.getRestaurantsTypeUuid())
          .orElseThrow(() -> new NotFoundException("Restaurant type not found"));
    }

    final var restaurantsUpdated = repository.update(uuid, mapper.toDomain(uuid, input, restaurantType));

    return mapper.toOutput(restaurantsUpdated);
  }
}
