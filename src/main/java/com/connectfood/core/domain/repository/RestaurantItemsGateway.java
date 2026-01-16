package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.commons.PageModel;

public interface RestaurantItemsGateway {

  RestaurantItems save(RestaurantItems restaurantItems);

  RestaurantItems update(UUID uuid, RestaurantItems restaurantItems);

  Optional<RestaurantItems> findByUuid(UUID uuid);

  PageModel<List<RestaurantItems>> findAll(UUID restaurantUuid, Integer page, Integer size, String sort,
      String direction);

  void delete(UUID uuid);
}
