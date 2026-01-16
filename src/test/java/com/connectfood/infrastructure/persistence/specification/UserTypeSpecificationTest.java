package com.connectfood.infrastructure.persistence.specification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTypeSpecificationTest {

  @Test
  @DisplayName("Deve criar specification para filtro por nome")
  void shouldCreateSpecificationForNameContains() {
    final var specification = UserTypeSpecification.nameContains("ADMIN");

    Assertions.assertNotNull(specification);
  }
}
