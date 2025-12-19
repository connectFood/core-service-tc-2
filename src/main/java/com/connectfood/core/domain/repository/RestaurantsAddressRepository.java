package com.connectfood.core.domain.repository;

import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.domain.model.commons.PageModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantsAddressRepository {

  RestaurantsAddress save(RestaurantsAddress restaurantsAddress);

  RestaurantsAddress update(UUID uuid, RestaurantsAddress restaurantsAddress, UUID addressTypeUuid);

  Optional<RestaurantsAddress> findByRestaurantsUuid(UUID uuid);

  PageModel<List<RestaurantsAddress>> findAll(UUID restaurantsUuid, UUID addressUuid, Integer page, Integer size,
      String sort, String direction);

  void delete(UUID uuid);
}
