package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.commons.PageModel;

public interface RestaurantsGateway {

  Restaurant save(Restaurant restaurant);

  Restaurant update(UUID uuid, Restaurant restaurant);

  Optional<Restaurant> findByUuid(UUID uuid);

  PageModel<List<Restaurant>> findAll(String name, UUID restaurantsTypeUuid, String street, String city,
      String state, Integer page, Integer size, String sort, String direction);

  void delete(UUID uuid);
}
