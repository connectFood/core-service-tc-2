package com.connectfood.core.infrastructure.persistence.jpa;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.core.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantItemsImagesRepository extends JpaCommonRepository<RestaurantItemsEntity, Long> {
}
