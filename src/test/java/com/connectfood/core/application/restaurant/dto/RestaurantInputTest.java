package com.connectfood.core.application.restaurant.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantInputTest {

  @Test
  @DisplayName("Deve criar RestaurantsInput com todos os campos preenchidos e retornar valores corretamente")
  void shouldCreateRestaurantsInputAndReturnValues() {
    final var name = "Restaurante Teste";
    final var restaurantsTypeUuid = UUID.randomUUID();
    final var usersUuid = UUID.randomUUID();

    final var openingHours = List.of(
        new RestaurantOpeningHourInput(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)),
        new RestaurantOpeningHourInput(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(19, 0))
    );

    final var address = new AddressInput(
        "Av. Paulista",
        "1000",
        "Apto 101",
        "Bela Vista",
        "São Paulo",
        "SP",
        "BR",
        "01310-100"
    );

    final var input = new RestaurantInput(
        name,
        restaurantsTypeUuid,
        openingHours,
        address,
        usersUuid
    );

    Assertions.assertNotNull(input);
    Assertions.assertEquals(name, input.getName());
    Assertions.assertEquals(restaurantsTypeUuid, input.getRestaurantsTypeUuid());
    Assertions.assertEquals(openingHours, input.getOpeningHours());
    Assertions.assertEquals(address, input.getAddress());
    Assertions.assertEquals(usersUuid, input.getUsersUuid());
  }

  @Test
  @DisplayName("Deve criar RestaurantsInput mesmo com campos nulos")
  void shouldCreateRestaurantsInputWithNullFields() {
    final var input = new RestaurantInput(
        null,
        null,
        null,
        null,
        null
    );

    Assertions.assertNotNull(input);
    Assertions.assertNull(input.getName());
    Assertions.assertNull(input.getRestaurantsTypeUuid());
    Assertions.assertNull(input.getOpeningHours());
    Assertions.assertNull(input.getAddress());
    Assertions.assertNull(input.getUsersUuid());
  }
}
