package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantsAddress;

public interface RestaurantAddressGateway {

  RestaurantsAddress save(RestaurantsAddress restaurantsAddress);

  Optional<RestaurantsAddress> findByRestaurantsUuid(UUID uuid);

  void delete(UUID uuid);

  boolean existsByRestaurantsUuid(UUID restaurantUuid);
}
