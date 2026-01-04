package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantsTypeTest {

  @Test
  @DisplayName("Deve criar um tipo de restaurante com UUID explícito e dados válidos")
  void shouldCreateRestaurantsTypeWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var name = "Fast Food";
    final var description = "Quick service restaurant";

    final var restaurantsType = new RestaurantsType(uuid, name, description);

    Assertions.assertEquals(uuid, restaurantsType.getUuid());
    Assertions.assertEquals(name, restaurantsType.getName());
    Assertions.assertEquals(description, restaurantsType.getDescription());
  }

  @Test
  @DisplayName("Deve criar um tipo de restaurante sem UUID explícito e dados válidos")
  void shouldCreateRestaurantsTypeWithoutExplicitUuid() {
    final var name = "Fast Food";
    final var description = "Quick service restaurant";

    final var restaurantsType = new RestaurantsType(name, description);

    Assertions.assertNotNull(restaurantsType.getUuid());
    Assertions.assertEquals(name, restaurantsType.getName());
    Assertions.assertEquals(description, restaurantsType.getDescription());
  }

  @Test
  @DisplayName("Não deve criar um tipo de restaurante sem name e lançar BadRequestException")
  void shouldNotCreateRestaurantsTypeWithoutNameAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantsType(UUID.randomUUID(), null, "desc")
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um tipo de restaurante com name em branco e lançar BadRequestException")
  void shouldNotCreateRestaurantsTypeWithNameBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantsType(UUID.randomUUID(), "   ", "desc")
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um tipo de restaurante com name menor que 3 caracteres e lançar BadRequestException")
  void shouldNotCreateRestaurantsTypeWithNameTooShortAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantsType(UUID.randomUUID(), "AB", "desc")
    );

    Assertions.assertEquals("Name length must be between 3 and 255 characters", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um tipo de restaurante com name maior que 255 caracteres e lançar BadRequestException")
  void shouldNotCreateRestaurantsTypeWithNameTooLongAndThrowBadRequestException() {
    final var name = "A".repeat(256);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantsType(UUID.randomUUID(), name, "desc")
    );

    Assertions.assertEquals("Name length must be between 3 and 255 characters", exception.getMessage());
  }
}
