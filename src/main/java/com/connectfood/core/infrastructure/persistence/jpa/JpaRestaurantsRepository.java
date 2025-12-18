package com.connectfood.core.infrastructure.persistence.jpa;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantsRepository extends JpaCommonRepository<RestaurantsEntity, Long> {
}
