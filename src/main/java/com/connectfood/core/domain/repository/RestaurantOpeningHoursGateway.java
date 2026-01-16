package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHours;

public interface RestaurantOpeningHoursGateway {

  RestaurantOpeningHours save(RestaurantOpeningHours model, final UUID restaurantUuid);

  RestaurantOpeningHours update(UUID uuid, RestaurantOpeningHours model);

  Optional<RestaurantOpeningHours> findByUuid(UUID uuid);

  void delete(UUID uuid);
}
