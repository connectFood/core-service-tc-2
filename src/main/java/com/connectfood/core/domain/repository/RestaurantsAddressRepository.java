package com.connectfood.core.domain.repository;

import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.model.commons.PageModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantsAddressRepository {

  RestaurantsAddress save(RestaurantsAddress restaurantsAddress);

  Optional<RestaurantsAddress> findByRestaurantsUuid(UUID uuid);

  void delete(UUID uuid);
}
