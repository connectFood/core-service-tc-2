package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantOpeningHoursRepository extends JpaCommonRepository<RestaurantOpeningHoursEntity, Long> {
}
