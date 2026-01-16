package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.model.commons.PageModel;

public interface RestaurantItemsGateway {

  RestaurantItem save(RestaurantItem restaurantItem);

  RestaurantItem update(UUID uuid, RestaurantItem restaurantItem);

  Optional<RestaurantItem> findByUuid(UUID uuid);

  PageModel<List<RestaurantItem>> findAll(UUID restaurantUuid, Integer page, Integer size, String sort,
      String direction);

  void delete(UUID uuid);
}
