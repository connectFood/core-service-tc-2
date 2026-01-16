package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItemImage;
import com.connectfood.core.domain.model.commons.PageModel;

public interface RestaurantItemImageGateway {

  RestaurantItemImage save(UUID restaurantItemsUuid, RestaurantItemImage restaurantItemImage);

  RestaurantItemImage update(UUID uuid, RestaurantItemImage restaurantItemImage);

  Optional<RestaurantItemImage> findByUuid(UUID uuid);

  PageModel<List<RestaurantItemImage>> findAll(UUID restaurantItemsUuid, Integer page, Integer size, String sort,
      String direction);

  void delete(UUID uuid);
}
