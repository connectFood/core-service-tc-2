package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.restaurants.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.repository.UsersRestaurantRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUsersRestaurantUseCase {

  private final UsersRestaurantRepository repository;
  private final UsersRepository usersRepository;
  private final RestaurantsRepository restaurantsRepository;
  private final UsersRestaurantAppMapper mapper;
  private final UsersAppMapper usersMapper;

  public CreateUsersRestaurantUseCase(
      final UsersRestaurantRepository repository,
      final UsersRepository usersRepository,
      final RestaurantsRepository restaurantsRepository,
      final UsersRestaurantAppMapper mapper,
      final UsersAppMapper usersMapper) {
    this.repository = repository;
    this.usersRepository = usersRepository;
    this.restaurantsRepository = restaurantsRepository;
    this.mapper = mapper;
    this.usersMapper = usersMapper;
  }

  @Transactional
  public UsersOutput execute(final UUID usersUuid, final UUID restaurantUuid) {
    final var users = usersRepository.findByUuid(usersUuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurants =
        restaurantsRepository.findByUuid(restaurantUuid)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var usersRestaurant = repository.save(mapper.toDomain(users, restaurants));

    return usersMapper.toOutput(usersRestaurant.getUser());
  }
}

