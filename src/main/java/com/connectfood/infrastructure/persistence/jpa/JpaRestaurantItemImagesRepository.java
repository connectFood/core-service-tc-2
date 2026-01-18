package com.connectfood.infrastructure.persistence.jpa;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemImageEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantItemImagesRepository extends JpaCommonRepository<RestaurantItemImageEntity, Long> {

  boolean existsByRestaurantItemsUuid(UUID restaurantItemUuid);

  void deleteByRestaurantItemsUuid(UUID restaurantItemUuid);
}
