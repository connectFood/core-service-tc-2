package com.connectfood.core.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.core.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class UsersSpecification {

  private UsersSpecification() {
  }

  public static Specification<UsersEntity> nameContains(final String name) {
    return likeIgnoreCase("fullName", name);
  }

  public static Specification<UsersEntity> emailContains(final String email) {
    return likeIgnoreCase("email", email);
  }

  public static Specification<UsersEntity> hasUsersTypeUuid(final UUID usersTypeUuid) {
    if (usersTypeUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("usersType")
        .get("uuid"), usersTypeUuid
    );
  }
}
