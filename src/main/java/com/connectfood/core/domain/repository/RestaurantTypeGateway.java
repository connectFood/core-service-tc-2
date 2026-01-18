package com.connectfood.core.domain.repository;

import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.commons.PageModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantTypeGateway {

  RestaurantType save(RestaurantType restaurantType);

  RestaurantType update(UUID uuid, RestaurantType restaurantType);

  Optional<RestaurantType> findById(UUID uuid);

  PageModel<List<RestaurantType>> findAll(String name, Integer page, Integer size, String sort, String direction);

  void delete(UUID uuid);

  boolean existsByName(String name);
}
