package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemImageEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantItemImagesRepository extends JpaCommonRepository<RestaurantItemImageEntity, Long> {
}
