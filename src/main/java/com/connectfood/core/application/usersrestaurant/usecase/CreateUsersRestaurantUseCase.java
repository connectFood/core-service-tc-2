package com.connectfood.core.application.usersrestaurant.usecase;

import com.connectfood.core.application.usersrestaurant.dto.UsersRestaurantInput;
import com.connectfood.core.application.usersrestaurant.dto.UsersRestaurantOutput;
import com.connectfood.core.application.usersrestaurant.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.domain.exception.BadRequestException;
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

  public CreateUsersRestaurantUseCase(
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
  public UsersRestaurantOutput execute(final UsersRestaurantInput input) {

    final var users =
        usersRepository.findByUuid(input.getUsersUuid())
            .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurants =
        restaurantsRepository.findByUuid(input.getRestaurantUuid())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    // Regra do seu diagrama: User tem apenas 1 UsersRestaurant
    if (repository.findByUsersUuid(users.getUuid()).isPresent()) {
      throw new BadRequestException("User already linked to a restaurant");
    }

    final var usersRestaurant = repository.save(mapper.toDomain(users, restaurants));

    return mapper.toOutput(usersRestaurant);
  }
}

