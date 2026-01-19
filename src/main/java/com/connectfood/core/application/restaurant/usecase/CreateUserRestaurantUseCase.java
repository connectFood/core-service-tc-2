package com.connectfood.core.application.restaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurant.mapper.UserRestaurantAppMapper;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserRestaurantGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUserRestaurantUseCase {

  private final UserRestaurantGateway repository;
  private final UserGateway userGateway;
  private final RestaurantGateway restaurantGateway;
  private final UserRestaurantAppMapper mapper;
  private final UserAppMapper usersMapper;

  public CreateUserRestaurantUseCase(
      final UserRestaurantGateway repository,
      final UserGateway userGateway,
      final RestaurantGateway restaurantGateway,
      final UserRestaurantAppMapper mapper,
      final UserAppMapper usersMapper) {
    this.repository = repository;
    this.userGateway = userGateway;
    this.restaurantGateway = restaurantGateway;
    this.mapper = mapper;
    this.usersMapper = usersMapper;
  }

  @Transactional
  public UserOutput execute(final UUID usersUuid, final UUID restaurantUuid) {
    final var users = userGateway.findByUuid(usersUuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurants =
        restaurantGateway.findByUuid(restaurantUuid)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var usersRestaurant = repository.save(mapper.toDomain(users, restaurants));

    return usersMapper.toOutput(usersRestaurant.getUser());
  }
}

