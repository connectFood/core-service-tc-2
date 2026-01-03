package com.connectfood.core.infrastructure.persistence.specification;


import com.connectfood.core.infrastructure.persistence.entity.RestaurantsAddressEntity;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static com.connectfood.core.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public class RestaurantsAddressSpecification {

  private RestaurantsAddressSpecification() {}

  public static Specification<RestaurantsAddressEntity> cityContains(
      final String city
  ) {
    return likeIgnoreCase("city", city);
  }

  public static Specification<RestaurantsAddressEntity> stateContains(
      final String state
  ) {
    return likeIgnoreCase("state", state);
  }

  public static Specification<RestaurantsAddressEntity> countryContains(
      final String country
  ) {
    return likeIgnoreCase("country", country);
  }

  public static Specification<RestaurantsAddressEntity> hasRestaurantsUuid(final UUID restaurantsUuid) {
    if(restaurantsUuid == null) {
      return null;
    }

    return((root, query, cb) -> cb.equal(root.get("restaurants")
        .get("uuid"), restaurantsUuid
    ));
  }
}
