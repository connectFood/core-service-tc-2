package com.connectfood.core.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantItemsImagesEntity;

import org.springframework.data.jpa.domain.Specification;

public final class RestaurantItemsImagesSpecification {

  private RestaurantItemsImagesSpecification() {
  }

  public static Specification<RestaurantItemsImagesEntity> hasRestaurantItemsUuid(final UUID restaurantItemsUuid) {
    if (restaurantItemsUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("restaurantItems")
        .get("uuid"), restaurantItemsUuid
    );
  }
}
