package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserRestaurantGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUsersRestaurantUseCase {

  private final UserRestaurantGateway repository;
  private final UserGateway userGateway;
  private final RestaurantGateway restaurantGateway;
  private final UsersRestaurantAppMapper mapper;
  private final UsersAppMapper usersMapper;

  public CreateUsersRestaurantUseCase(
      final UserRestaurantGateway repository,
      final UserGateway userGateway,
      final RestaurantGateway restaurantGateway,
      final UsersRestaurantAppMapper mapper,
      final UsersAppMapper usersMapper) {
    this.repository = repository;
    this.userGateway = userGateway;
    this.restaurantGateway = restaurantGateway;
    this.mapper = mapper;
    this.usersMapper = usersMapper;
  }

  @Transactional
  public UsersOutput execute(final UUID usersUuid, final UUID restaurantUuid) {
    final var users = userGateway.findByUuid(usersUuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurants =
        restaurantGateway.findByUuid(restaurantUuid)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var usersRestaurant = repository.save(mapper.toDomain(users, restaurants));

    return usersMapper.toOutput(usersRestaurant.getUser());
  }
}

