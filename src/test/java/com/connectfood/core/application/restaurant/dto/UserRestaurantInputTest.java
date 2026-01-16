package com.connectfood.core.application.restaurant.dto;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRestaurantInputTest {

  @Test
  @DisplayName("Deve criar UsersRestaurantInput com todos os campos preenchidos")
  void shouldCreateUsersRestaurantInputWithAllFields() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final var input = new UserRestaurantInput(usersUuid, restaurantUuid);

    Assertions.assertNotNull(input);
    Assertions.assertEquals(usersUuid, input.getUsersUuid());
    Assertions.assertEquals(restaurantUuid, input.getRestaurantUuid());
  }

  @Test
  @DisplayName("Deve criar UsersRestaurantInput mesmo com campos nulos")
  void shouldCreateUsersRestaurantInputWithNullFields() {
    final var input = new UserRestaurantInput(null, null);

    Assertions.assertNotNull(input);
    Assertions.assertNull(input.getUsersUuid());
    Assertions.assertNull(input.getRestaurantUuid());
  }
}
