package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantTypeRepository extends JpaCommonRepository<RestaurantTypeEntity, Long> {

  boolean existsByName(String name);
}
