package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.commons.PageModel;

public interface RestaurantOpeningHoursRepository {

  RestaurantOpeningHours save(RestaurantOpeningHours model, final UUID restaurantUuid);

  RestaurantOpeningHours update(UUID uuid, RestaurantOpeningHours model);

  Optional<RestaurantOpeningHours> findByUuid(UUID uuid);

  PageModel<List<RestaurantOpeningHours>> findAll(UUID restaurantUuid, Integer page, Integer size, String sort,
      String direction);

  void delete(UUID uuid);
}
