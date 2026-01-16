package com.connectfood.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class RestaurantsSpecification {

  private RestaurantsSpecification() {
  }

  public static Specification<RestaurantsEntity> nameContains(final String name) {
    return likeIgnoreCase("name", name);
  }

  public static Specification<RestaurantsEntity> hasRestaurantsTypeUuid(final UUID restaurantsTypeUuid) {
    if (restaurantsTypeUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("restaurantsType")
        .get("uuid"), restaurantsTypeUuid
    );
  }

  public static Specification<RestaurantsEntity> cityContains(final String city) {
    if (city == null) {
      return null;
    }

    final String pattern = "%" + city.trim()
        .toLowerCase() + "%";

    return (root, query, cb) -> cb.like(cb.lower(root.get("address")
        .get("city")), pattern
    );
  }

  public static Specification<RestaurantsEntity> stateContains(final String state) {
    if (state == null) {
      return null;
    }

    final String pattern = "%" + state.trim()
        .toLowerCase() + "%";

    return (root, query, cb) -> cb.like(cb.lower(root.get("address")
        .get("state")), pattern
    );
  }

  public static Specification<RestaurantsEntity> streetContains(final String street) {
    if (street == null) {
      return null;
    }

    final String pattern = "%" + street.trim()
        .toLowerCase() + "%";

    return (root, query, cb) -> cb.like(cb.lower(root.get("address")
        .get("street")), pattern
    );
  }
}
