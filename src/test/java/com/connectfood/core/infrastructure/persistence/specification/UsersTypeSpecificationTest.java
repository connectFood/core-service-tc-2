package com.connectfood.core.infrastructure.persistence.specification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersTypeSpecificationTest {

  @Test
  @DisplayName("Deve criar specification para filtro por nome")
  void shouldCreateSpecificationForNameContains() {
    final var specification = UsersTypeSpecification.nameContains("ADMIN");

    Assertions.assertNotNull(specification);
  }
}
