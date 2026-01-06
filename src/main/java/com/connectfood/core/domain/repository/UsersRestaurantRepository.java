package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersRestaurant;

public interface UsersRestaurantRepository {

  UsersRestaurant save(UsersRestaurant usersRestaurant);

  Optional<UsersRestaurant> findByUsersUuid(UUID usersUuid);

  void delete(UUID uuid);
}
