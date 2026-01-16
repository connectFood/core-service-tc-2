package com.connectfood.infrastructure.persistence.specification;

import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class RestaurantTypeSpecification {

  private RestaurantTypeSpecification() {
  }

  public static Specification<RestaurantTypeEntity> nameContains(final String name) {
    return likeIgnoreCase("name", name);
  }
}
