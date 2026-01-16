package com.connectfood.infrastructure.persistence.specification;

import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.UserEntity;

import org.springframework.data.jpa.domain.Specification;

import static com.connectfood.infrastructure.persistence.specification.commons.SpecificationCommons.likeIgnoreCase;

public final class UserSpecification {

  private UserSpecification() {
  }

  public static Specification<UserEntity> nameContains(final String name) {
    return likeIgnoreCase("fullName", name);
  }

  public static Specification<UserEntity> emailContains(final String email) {
    return likeIgnoreCase("email", email);
  }

  public static Specification<UserEntity> hasUsersTypeUuid(final UUID usersTypeUuid) {
    if (usersTypeUuid == null) {
      return null;
    }

    return (root, query, cb) -> cb.equal(root.get("usersType")
        .get("uuid"), usersTypeUuid
    );
  }
}
