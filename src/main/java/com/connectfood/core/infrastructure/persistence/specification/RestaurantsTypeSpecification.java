package com.connectfood.core.infrastructure.persistence.specification;

import com.connectfood.core.infrastructure.persistence.entity.RestaurantsTypeEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.core.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class RestaurantsTypeSpecification {

  private RestaurantsTypeSpecification() {
  }

  public static Specification<RestaurantsTypeEntity> nameContains(final String name) {
    return likeIgnoreCase("name", name);
  }
}
