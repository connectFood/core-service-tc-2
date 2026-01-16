package com.connectfood.infrastructure.persistence.specification;

import com.connectfood.infrastructure.persistence.specification.RestaurantsTypeSpecification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsTypeSpecificationTest {

  @Test
  @DisplayName("Deve criar specification para filtro por nome")
  void shouldCreateSpecificationForNameContains() {
    final var specification = RestaurantsTypeSpecification.nameContains("Fast Food");

    Assertions.assertNotNull(specification);
  }
}
