package com.connectfood.infrastructure.persistence.jpa;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantRepository extends JpaCommonRepository<RestaurantEntity, Long> {

  boolean existsByNameAndRestaurantsTypeUuidAndUsersUuid(String name, UUID restaurantsTypeUuid, UUID usersUuid);
}
