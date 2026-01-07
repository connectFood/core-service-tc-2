package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersRestaurant;
import com.connectfood.core.domain.model.commons.PageModel;

public interface UsersRestaurantRepository {

  UsersRestaurant save(UsersRestaurant usersRestaurant);

  UsersRestaurant update(UUID uuid, UsersRestaurant usersRestaurant);

  Optional<UsersRestaurant> findById(UUID uuid);

  Optional<UsersRestaurant> findByUsersUuid(UUID usersUuid);

  PageModel<List<UsersRestaurant>> findAllByRestaurantUuid(
      UUID restaurantUuid,
      Integer page,
      Integer size,
      String sort,
      String direction
  );

  void delete(UUID uuid);
}
