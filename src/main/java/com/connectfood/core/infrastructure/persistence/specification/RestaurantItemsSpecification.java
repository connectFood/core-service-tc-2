package com.connectfood.core.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantItemsEntity;

import org.springframework.data.jpa.domain.Specification;

public final class RestaurantItemsSpecification {

  private RestaurantItemsSpecification() {
  }

  public static Specification<RestaurantItemsEntity> hasRestaurantUuid(final UUID restaurantUuid) {
    if (restaurantUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("restaurant")
        .get("uuid"), restaurantUuid
    );
  }
}
