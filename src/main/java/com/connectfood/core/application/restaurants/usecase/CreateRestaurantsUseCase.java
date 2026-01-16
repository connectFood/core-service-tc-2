package com.connectfood.core.application.restaurants.usecase;

import java.util.ArrayList;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
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
public class CreateRestaurantsUseCase {

  private final RestaurantGateway repository;
  private final RestaurantsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantTypeGateway restaurantTypeGateway;
  private final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;
  private final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;
  private final UserGateway userGateway;
  private final CreateUsersRestaurantUseCase createUsersRestaurantUseCase;

  public CreateRestaurantsUseCase(
      final RestaurantGateway repository,
      final RestaurantsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantTypeGateway restaurantTypeGateway,
      final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase,
      final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase,
      final UserGateway userGateway,
      final CreateUsersRestaurantUseCase createUsersRestaurantUseCase
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantTypeGateway = restaurantTypeGateway;
    this.createRestaurantsAddressUseCase = createRestaurantsAddressUseCase;
    this.createRestaurantOpeningHoursUseCase = createRestaurantOpeningHoursUseCase;
    this.userGateway = userGateway;
    this.createUsersRestaurantUseCase = createUsersRestaurantUseCase;
  }

  @Transactional
  public RestaurantsOutput execute(final RequestUser requestUser, final RestaurantsInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());

    final var users = userGateway.findByUuid(input.getUsersUuid())
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurantsType = restaurantTypeGateway
        .findById(input.getRestaurantsTypeUuid())
        .orElseThrow(() -> new NotFoundException("Restaurants Type not found"));

    final var restaurants = repository.save(mapper.toDomain(input, restaurantsType));

    final var address = createRestaurantsAddressUseCase.execute(requestUser, restaurants.getUuid(), input.getAddress());

    final var usersPersisted = createUsersRestaurantUseCase.execute(users.getUuid(), restaurants.getUuid());

    var openingHours = new ArrayList<RestaurantOpeningHoursOutput>();
    for (var openingHour : input.getOpeningHours()) {
      openingHours.add(createRestaurantOpeningHoursUseCase.execute(requestUser, restaurants.getUuid(), openingHour));
    }

    return mapper.toOutput(restaurants, openingHours, address, usersPersisted);
  }
}
