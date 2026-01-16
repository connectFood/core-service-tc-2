package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.model.commons.PageModel;

public interface RestaurantItemsImagesGateway {

  RestaurantItemsImages save(UUID restaurantItemsUuid, RestaurantItemsImages restaurantItemsImages);

  RestaurantItemsImages update(UUID uuid, RestaurantItemsImages restaurantItemsImages);

  Optional<RestaurantItemsImages> findByUuid(UUID uuid);

  PageModel<List<RestaurantItemsImages>> findAll(UUID restaurantItemsUuid, Integer page, Integer size, String sort,
      String direction);

  void delete(UUID uuid);
}
