package com.connectfood.core.application.restaurants.dto;

import java.util.UUID;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantsOutputTest {

  @Test
  @DisplayName("Deve criar RestaurantsOutput e retornar valores corretamente")
  void shouldCreateRestaurantsOutputAndReturnValues() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurante Teste";
    final RestaurantsTypeOutput restaurantsType = new RestaurantsTypeOutput(
        UUID.randomUUID(),
        "Fast Food",
        "Fast food restaurant"
    );

    final var output = new RestaurantsOutput(uuid, name, restaurantsType);

    Assertions.assertNotNull(output);
    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(name, output.getName());
    Assertions.assertEquals(restaurantsType, output.getRestaurantsType());
  }
}
