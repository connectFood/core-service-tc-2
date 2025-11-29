package com.connectfood.core.infrastructure.persistence.specification;

import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.core.infrastructure.persistence.specification.commons.SpecificationCommons.eq;
import static com.connectfood.core.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class UsersSpecification {

  private UsersSpecification() {
  }

  public static Specification<UsersEntity> nameContains(String name) {
    return likeIgnoreCase("fullName", name);
  }

  public static Specification<UsersEntity> emailContains(String email) {
    return likeIgnoreCase("email", email);
  }

  public static Specification<UsersEntity> hasUsersTypeId(Long usersTypeId) {
    return eq("usersType.id", usersTypeId);
  }
}
