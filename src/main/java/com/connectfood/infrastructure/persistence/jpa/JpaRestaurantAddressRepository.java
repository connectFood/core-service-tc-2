package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.RestaurantAddressEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaRestaurantAddressRepository extends JpaCommonRepository<RestaurantAddressEntity, Long> {

  Optional<RestaurantAddressEntity> findByRestaurantsUuid(UUID restaurantsUuid);
}
