package com.connectfood.core.domain.repository;

import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.domain.model.commons.PageModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantsTypeGateway {

  RestaurantsType save(RestaurantsType restaurantType);

  RestaurantsType update(UUID uuid, RestaurantsType restaurantType);

  Optional<RestaurantsType> findById(UUID uuid);

  PageModel<List<RestaurantsType>> findAll(String name, Integer page, Integer size, String sort, String direction);

  void delete(UUID uuid);
}
