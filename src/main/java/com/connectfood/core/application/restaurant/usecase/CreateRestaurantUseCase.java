package com.connectfood.core.application.restaurant.usecase;

import java.util.ArrayList;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.application.restaurant.dto.RestaurantInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;
import com.connectfood.core.domain.repository.UserGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantUseCase {

  private final RestaurantGateway repository;
  private final RestaurantAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantTypeGateway restaurantTypeGateway;
  private final CreateRestaurantAddressUseCase createRestaurantAddressUseCase;
  private final CreateRestaurantOpeningHourUseCase createRestaurantOpeningHourUseCase;
  private final UserGateway userGateway;
  private final CreateUserRestaurantUseCase createUserRestaurantUseCase;

  public CreateRestaurantUseCase(
      final RestaurantGateway repository,
      final RestaurantAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantTypeGateway restaurantTypeGateway,
      final CreateRestaurantAddressUseCase createRestaurantAddressUseCase,
      final CreateRestaurantOpeningHourUseCase createRestaurantOpeningHourUseCase,
      final UserGateway userGateway,
      final CreateUserRestaurantUseCase createUserRestaurantUseCase
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantTypeGateway = restaurantTypeGateway;
    this.createRestaurantAddressUseCase = createRestaurantAddressUseCase;
    this.createRestaurantOpeningHourUseCase = createRestaurantOpeningHourUseCase;
    this.userGateway = userGateway;
    this.createUserRestaurantUseCase = createUserRestaurantUseCase;
  }

  @Transactional
  public RestaurantOutput execute(final RequestUser requestUser, final RestaurantInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());

    final var users = userGateway.findByUuid(input.getUsersUuid())
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurantsType = restaurantTypeGateway
        .findById(input.getRestaurantsTypeUuid())
        .orElseThrow(() -> new NotFoundException("Restaurants Type not found"));

    final var restaurants = repository.save(mapper.toDomain(input, restaurantsType));

    final var address = createRestaurantAddressUseCase.execute(requestUser, restaurants.getUuid(), input.getAddress());

    final var usersPersisted = createUserRestaurantUseCase.execute(users.getUuid(), restaurants.getUuid());

    var openingHours = new ArrayList<RestaurantOpeningHourOutput>();
    for (var openingHour : input.getOpeningHours()) {
      openingHours.add(createRestaurantOpeningHourUseCase.execute(requestUser, restaurants.getUuid(), openingHour));
    }

    return mapper.toOutput(restaurants, openingHours, address, usersPersisted);
  }
}
