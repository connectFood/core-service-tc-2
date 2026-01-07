package com.connectfood.core.application.usersrestaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.usersrestaurant.dto.UsersRestaurantOutput;
import com.connectfood.core.application.usersrestaurant.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersRestaurantRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FindUsersRestaurantUseCase {

  private final UsersRestaurantRepository repository;
  private final UsersRestaurantAppMapper mapper;

  public FindUsersRestaurantUseCase(
      final UsersRestaurantRepository repository,
      final UsersRestaurantAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public UsersRestaurantOutput execute(final UUID usersUuid) {

    final var usersRestaurant =
        repository.findByUsersUuid(usersUuid)
            .orElseThrow(() -> new NotFoundException("Users restaurant not found"));

    return mapper.toOutput(usersRestaurant);
  }
}
