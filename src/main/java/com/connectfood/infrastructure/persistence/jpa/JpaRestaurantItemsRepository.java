package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemsEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantItemsRepository extends JpaCommonRepository<RestaurantItemsEntity, Long> {
}
