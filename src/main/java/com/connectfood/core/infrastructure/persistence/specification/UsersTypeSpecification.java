package com.connectfood.core.infrastructure.persistence.specification;

import com.connectfood.core.infrastructure.persistence.entity.UsersTypeEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.core.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class UsersTypeSpecification {

  private UsersTypeSpecification() {
  }

  public static Specification<UsersTypeEntity> nameContains(final String name) {
    return likeIgnoreCase("name", name);
  }
}
