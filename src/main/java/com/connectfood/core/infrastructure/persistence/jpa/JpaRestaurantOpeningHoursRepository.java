package com.connectfood.core.infrastructure.persistence.jpa;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.core.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantOpeningHoursRepository extends JpaCommonRepository<RestaurantOpeningHoursEntity, Long> {
}
