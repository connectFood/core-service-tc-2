package com.connectfood.infrastructure.persistence.jpa;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.UserRestaurantEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUserRestaurantRepository extends JpaCommonRepository<UserRestaurantEntity, Long> {

  boolean existsByRestaurantsUuid(UUID restaurantUuid);

  void deleteByRestaurantsUuid(UUID restaurantUuid);
}
