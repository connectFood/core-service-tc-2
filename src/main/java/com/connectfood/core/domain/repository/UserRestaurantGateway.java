package com.connectfood.core.domain.repository;

import java.util.UUID;

import com.connectfood.core.domain.model.UserRestaurant;

public interface UserRestaurantGateway {

  UserRestaurant save(UserRestaurant userRestaurant);

  boolean existsByRestaurantsUuid(UUID restaurantUuid);

  void deleteByRestaurantsUuid(UUID restaurantUuid);
}
