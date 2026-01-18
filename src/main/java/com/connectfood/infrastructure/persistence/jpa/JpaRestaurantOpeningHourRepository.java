package com.connectfood.infrastructure.persistence.jpa;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHourEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantOpeningHourRepository extends JpaCommonRepository<RestaurantOpeningHourEntity, Long> {

  boolean existsByRestaurantUuid(UUID uuid);

  void deleteByRestaurantUuid(UUID uuid);
}
