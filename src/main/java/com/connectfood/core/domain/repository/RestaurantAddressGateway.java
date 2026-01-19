package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantAddress;

public interface RestaurantAddressGateway {

  RestaurantAddress save(RestaurantAddress restaurantAddress);

  Optional<RestaurantAddress> findByRestaurantsUuid(UUID uuid);

  void delete(UUID uuid);

  boolean existsByRestaurantsUuid(UUID restaurantUuid);
}
