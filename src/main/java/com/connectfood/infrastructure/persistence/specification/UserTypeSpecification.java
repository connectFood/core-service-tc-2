package com.connectfood.infrastructure.persistence.specification;

import com.connectfood.infrastructure.persistence.entity.UserTypeEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class UserTypeSpecification {

  private UserTypeSpecification() {
  }

  public static Specification<UserTypeEntity> nameContains(final String name) {
    return likeIgnoreCase("name", name);
  }
}
