package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHourEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantOpeningHourRepository extends JpaCommonRepository<RestaurantOpeningHourEntity, Long> {
}
