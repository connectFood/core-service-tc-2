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
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;
import com.connectfood.core.domain.repository.UsersRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantsUseCase {

  private final RestaurantsRepository repository;
  private final RestaurantsAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantsTypeRepository restaurantsTypeRepository;
  private final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase;
  private final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase;
  private final UsersRepository usersRepository;
  private final CreateUsersRestaurantUseCase createUsersRestaurantUseCase;

  public CreateRestaurantsUseCase(
      final RestaurantsRepository repository,
      final RestaurantsAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantsTypeRepository restaurantsTypeRepository,
      final CreateRestaurantsAddressUseCase createRestaurantsAddressUseCase,
      final CreateRestaurantOpeningHoursUseCase createRestaurantOpeningHoursUseCase,
      final UsersRepository usersRepository,
      final CreateUsersRestaurantUseCase createUsersRestaurantUseCase
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantsTypeRepository = restaurantsTypeRepository;
    this.createRestaurantsAddressUseCase = createRestaurantsAddressUseCase;
    this.createRestaurantOpeningHoursUseCase = createRestaurantOpeningHoursUseCase;
    this.usersRepository = usersRepository;
    this.createUsersRestaurantUseCase = createUsersRestaurantUseCase;
  }

  @Transactional
  public RestaurantsOutput execute(final RequestUser requestUser, final RestaurantsInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());

    final var users = usersRepository.findByUuid(input.getUsersUuid())
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var restaurantsType = restaurantsTypeRepository
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
