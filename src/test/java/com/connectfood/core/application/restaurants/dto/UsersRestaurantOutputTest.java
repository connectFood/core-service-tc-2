package com.connectfood.core.application.restaurants.dto;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersRestaurantOutputTest {

  @Test
  @DisplayName("Deve criar UsersRestaurantOutput com todos os campos preenchidos")
  void shouldCreateUsersRestaurantOutputWithAllFields() {
    final var uuid = UUID.randomUUID();
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final var output = new UsersRestaurantOutput(
        uuid,
        usersUuid,
        restaurantUuid
    );

    Assertions.assertNotNull(output);
    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(usersUuid, output.getUsersUuid());
    Assertions.assertEquals(restaurantUuid, output.getRestaurantUuid());
  }

  @Test
  @DisplayName("Deve criar UsersRestaurantOutput mesmo com campos nulos")
  void shouldCreateUsersRestaurantOutputWithNullFields() {
    final var output = new UsersRestaurantOutput(
        null,
        null,
        null
    );

    Assertions.assertNotNull(output);
    Assertions.assertNull(output.getUuid());
    Assertions.assertNull(output.getUsersUuid());
    Assertions.assertNull(output.getRestaurantUuid());
  }
}
