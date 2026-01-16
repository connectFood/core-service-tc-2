package com.connectfood.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemEntity;

import org.springframework.data.jpa.domain.Specification;

public final class RestaurantItemSpecification {

  private RestaurantItemSpecification() {
  }

  public static Specification<RestaurantItemEntity> hasRestaurantUuid(final UUID restaurantUuid) {
    if (restaurantUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("restaurant")
        .get("uuid"), restaurantUuid
    );
  }
}
