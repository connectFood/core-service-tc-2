package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.commons.PageModel;

public interface RestaurantsRepository {

  Restaurants save(Restaurants restaurant);

  Restaurants update(UUID uuid, Restaurants restaurants);

  Optional<Restaurants> findByUuid(UUID uuid);

  PageModel<List<Restaurants>> findAll(String name, UUID restaurantsTypeUuid, String street, String city,
      String state, Integer page, Integer size, String sort, String direction);

  void delete(UUID uuid);
}
