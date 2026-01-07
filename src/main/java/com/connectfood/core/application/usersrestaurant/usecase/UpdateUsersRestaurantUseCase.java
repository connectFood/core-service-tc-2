package com.connectfood.core.application.usersrestaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.usersrestaurant.dto.UsersRestaurantInput;
import com.connectfood.core.application.usersrestaurant.dto.UsersRestaurantOutput;
import com.connectfood.core.application.usersrestaurant.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.repository.UsersRestaurantRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUsersRestaurantUseCase {

  private final UsersRestaurantRepository repository;
  private final UsersRepository usersRepository;
  private final RestaurantsRepository restaurantsRepository;
  private final UsersRestaurantAppMapper mapper;

  public UpdateUsersRestaurantUseCase(
      final UsersRestaurantRepository repository,
      final UsersRepository usersRepository,
      final RestaurantsRepository restaurantsRepository,
      final UsersRestaurantAppMapper mapper) {
    this.repository = repository;
    this.usersRepository = usersRepository;
    this.restaurantsRepository = restaurantsRepository;
    this.mapper = mapper;
  }

  @Transactional
  public UsersRestaurantOutput execute(final UUID uuid, final UsersRestaurantInput input) {

    repository.findById(uuid)
        .orElseThrow(() -> new NotFoundException("UsersRestaurant Not Found " + uuid));

    final var user =
        usersRepository.findByUuid(input.getUsersUuid())
            .orElseThrow(() -> new NotFoundException("User not found " + input.getUsersUuid()));

    final var restaurant =
        restaurantsRepository.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found " + input.getRestaurantUuid()));

    final var usersRestaurant = repository.update(uuid, mapper.toDomain(uuid, user, restaurant));

    return mapper.toOutput(usersRestaurant);
  }
}
