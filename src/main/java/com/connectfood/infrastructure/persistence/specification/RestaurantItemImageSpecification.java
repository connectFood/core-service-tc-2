package com.connectfood.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.RestaurantItemImageEntity;

import org.springframework.data.jpa.domain.Specification;

public final class RestaurantItemImageSpecification {

  private RestaurantItemImageSpecification() {
  }

  public static Specification<RestaurantItemImageEntity> hasRestaurantItemsUuid(final UUID restaurantItemsUuid) {
    if (restaurantItemsUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("restaurantItems")
        .get("uuid"), restaurantItemsUuid
    );
  }
}
