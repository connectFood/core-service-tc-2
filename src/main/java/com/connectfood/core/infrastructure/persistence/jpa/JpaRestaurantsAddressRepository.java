package com.connectfood.core.infrastructure.persistence.jpa;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantsAddressEntity;
import com.connectfood.core.infrastructure.persistence.jpa.commons.JpaCommonRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaRestaurantsAddressRepository extends JpaCommonRepository<RestaurantsAddressEntity, Long> {

  Optional<RestaurantsAddressEntity> findByRestaurantsUuid(UUID restaurantsUuid);
}
