package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.RestaurantsAddressEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaRestaurantsAddressRepository extends JpaCommonRepository<RestaurantsAddressEntity, Long> {

  Optional<RestaurantsAddressEntity> findByRestaurantsUuid(UUID restaurantsUuid);
}
