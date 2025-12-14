package com.connectfood.core.infrastructure.persistence.specification;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static com.connectfood.core.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class RestaurantsSpecification {

  private RestaurantsSpecification(){}

  public static Specification<RestaurantsEntity> nameContains(final String name) {
    return likeIgnoreCase("name", name);
  }

  public static Specification<RestaurantsEntity> hasRestaurantsTypeUuid(final UUID restaurantsTypeUuid) {
    if(restaurantsTypeUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("restaurantsType")
        .get("uuid"), restaurantsTypeUuid
    );
  }
}
