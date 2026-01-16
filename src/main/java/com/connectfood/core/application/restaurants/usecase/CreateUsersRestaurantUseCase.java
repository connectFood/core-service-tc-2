package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsGateway;
import com.connectfood.core.domain.repository.UsersGateway;
import com.connectfood.core.domain.repository.UsersRestaurantGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUsersRestaurantUseCase {

  private final UsersRestaurantGateway repository;
  private final UsersGateway usersGateway;
  private final RestaurantsGateway restaurantsGateway;
  private final UsersRestaurantAppMapper mapper;
  private final UsersAppMapper usersMapper;

  public CreateUsersRestaurantUseCase(
      final UsersRestaurantGateway repository,
      final UsersGateway usersGateway,
      final RestaurantsGateway restaurantsGateway,
      final UsersRestaurantAppMapper mapper,
      final UsersAppMapper usersMapper) {
    this.repository = repository;
    this.usersGateway = usersGateway;
    this.restaurantsGateway = restaurantsGateway;
    this.mapper = mapper;
    this.usersMapper = usersMapper;
  }

  @Transactional
  public UsersOutput execute(final UUID usersUuid, final UUID restaurantUuid) {
    final var users = usersGateway.findByUuid(usersUuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurants =
        restaurantsGateway.findByUuid(restaurantUuid)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var usersRestaurant = repository.save(mapper.toDomain(users, restaurants));

    return usersMapper.toOutput(usersRestaurant.getUser());
  }
}

