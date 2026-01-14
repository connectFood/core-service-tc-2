package com.connectfood.core.application.restaurants.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.users.dto.UsersOutput;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantsOutputTest {

  @Test
  @DisplayName("Deve criar RestaurantsOutput com construtor simplificado e retornar valores corretamente")
  void shouldCreateRestaurantsOutputUsingSimplifiedConstructor() {
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
    Assertions.assertNull(output.getOpeningHours());
    Assertions.assertNull(output.getAddress());
    Assertions.assertNull(output.getUsers());
  }

  @Test
  @DisplayName("Deve criar RestaurantsOutput com todos os campos e retornar valores corretamente")
  void shouldCreateRestaurantsOutputUsingFullConstructor() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurante Completo";

    final RestaurantsTypeOutput restaurantsType = new RestaurantsTypeOutput(
        UUID.randomUUID(),
        "À la carte",
        "Restaurant with table service"
    );

    final var openingHours = List.of(
        new RestaurantOpeningHoursOutput(
            UUID.randomUUID(),
            DayOfWeek.MONDAY,
            LocalTime.of(9, 0),
            LocalTime.of(18, 0)
        ),
        new RestaurantOpeningHoursOutput(
            UUID.randomUUID(),
            DayOfWeek.TUESDAY,
            LocalTime.of(10, 0),
            LocalTime.of(19, 0)
        )
    );

    final AddressOutput address = Mockito.mock(AddressOutput.class);
    final UsersOutput users = Mockito.mock(UsersOutput.class);

    final var output = new RestaurantsOutput(
        uuid,
        name,
        restaurantsType,
        openingHours,
        address,
        users
    );

    Assertions.assertNotNull(output);
    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(name, output.getName());
    Assertions.assertEquals(restaurantsType, output.getRestaurantsType());
    Assertions.assertEquals(openingHours, output.getOpeningHours());
    Assertions.assertEquals(address, output.getAddress());
    Assertions.assertEquals(users, output.getUsers());
  }

  @Test
  @DisplayName("Deve criar RestaurantsOutput mesmo com campos nulos")
  void shouldCreateRestaurantsOutputWithNullFields() {
    final var output = new RestaurantsOutput(
        null,
        null,
        null,
        null,
        null,
        null
    );

    Assertions.assertNotNull(output);
    Assertions.assertNull(output.getUuid());
    Assertions.assertNull(output.getName());
    Assertions.assertNull(output.getRestaurantsType());
    Assertions.assertNull(output.getOpeningHours());
    Assertions.assertNull(output.getAddress());
    Assertions.assertNull(output.getUsers());
  }
}
