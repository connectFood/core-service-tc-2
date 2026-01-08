package com.connectfood.core.application.usersrestaurant.usecase;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.dto.commons.PageOutput;
import com.connectfood.core.application.usersrestaurant.dto.UsersRestaurantOutput;
import com.connectfood.core.application.usersrestaurant.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.domain.repository.UsersRestaurantRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SearchUsersRestaurantUseCase {

  private final UsersRestaurantRepository repository;
  private final UsersRestaurantAppMapper mapper;

  public SearchUsersRestaurantUseCase(
      final UsersRestaurantRepository repository,
      final UsersRestaurantAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public PageOutput<List<UsersRestaurantOutput>> execute(
      final UUID restaurantUuid,
      final Integer page,
      final Integer size,
      final String sort,
      final String direction) {

    final var usersRestaurants =
        repository.findAllByRestaurantUuid(restaurantUuid, page, size, sort, direction);

    final var results = usersRestaurants.content()
        .stream()
        .map(mapper::toOutput)
        .toList();

    return new PageOutput<>(results, usersRestaurants.total());
  }
}
