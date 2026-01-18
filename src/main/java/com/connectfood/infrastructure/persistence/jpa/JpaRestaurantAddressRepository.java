package com.connectfood.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantAddressEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaRestaurantAddressRepository extends JpaCommonRepository<RestaurantAddressEntity, Long> {

  Optional<RestaurantAddressEntity> findByRestaurantsUuid(UUID restaurantsUuid);

  boolean existsByRestaurantsUuid(UUID restaurantsUuid);
}
