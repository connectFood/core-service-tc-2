package com.connectfood.core.domain.repository;

import com.connectfood.core.domain.model.RestaurantsAddress;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantsAddressGateway {

  RestaurantsAddress save(RestaurantsAddress restaurantsAddress);

  Optional<RestaurantsAddress> findByRestaurantsUuid(UUID uuid);

  void delete(UUID uuid);
}
