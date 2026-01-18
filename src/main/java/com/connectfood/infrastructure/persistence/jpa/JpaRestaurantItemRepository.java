package com.connectfood.infrastructure.persistence.jpa;

import java.util.List;
import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantItemRepository extends JpaCommonRepository<RestaurantItemEntity, Long> {

  boolean existsByRestaurantUuid(UUID restaurantUuid);

  List<RestaurantItemEntity> findAllByRestaurantUuid(UUID restaurantUuid);
}
