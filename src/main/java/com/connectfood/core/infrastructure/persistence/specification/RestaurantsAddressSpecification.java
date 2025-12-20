package com.connectfood.core.infrastructure.persistence.specification;


import com.connectfood.core.infrastructure.persistence.entity.RestaurantsAddressEntity;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class RestaurantsAddressSpecification {

  private RestaurantsAddressSpecification() {}

  public static Specification<RestaurantsAddressEntity> hasRestaurantsUuid(final UUID restaurantsUuid) {
    if(restaurantsUuid == null) {
      return null;
    }

    return((root, query, cb) -> cb.equal(root.get("restaurants")
        .get("uuid"), restaurantsUuid
    ));
  }

  public static Specification<RestaurantsAddressEntity> hasAddressUuid(final UUID addressUuid) {
    if(addressUuid == null) {
      return null;
    }

    return ((root, query, cb) -> cb.equal(root.get("address")
        .get("uuid"), addressUuid
    ));
  }
}
