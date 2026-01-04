package com.connectfood.core.application.restaurants.dto;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantsInputTest {

  @Test
  @DisplayName("Deve criar RestaurantsInput e retornar valores corretamente")
  void shouldCreateRestaurantsInputAndReturnValues() {
    final var name = "Restaurante Teste";
    final var restaurantsTypeUuid = UUID.randomUUID();

    final var input = new RestaurantsInput(name, restaurantsTypeUuid);

    Assertions.assertNotNull(input);
    Assertions.assertEquals(name, input.getName());
    Assertions.assertEquals(restaurantsTypeUuid, input.getRestaurantsTypeUuid());
  }
}
