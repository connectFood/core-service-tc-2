package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHour;

public interface RestaurantOpeningHourGateway {

  RestaurantOpeningHour save(RestaurantOpeningHour model, final UUID restaurantUuid);

  RestaurantOpeningHour update(UUID uuid, RestaurantOpeningHour model);

  Optional<RestaurantOpeningHour> findByUuid(UUID uuid);

  void delete(UUID uuid);

  boolean existsByRestaurantUuid(UUID restaurantUuid);

  void deleteByRestaurantUuid(UUID restaurantUuid);
}
