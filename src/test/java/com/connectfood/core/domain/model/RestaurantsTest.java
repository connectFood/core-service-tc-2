package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

class RestaurantsTest {

  @Test
  @DisplayName("Deve criar um restaurante com UUID explícito e dados válidos")
  void shouldCreateRestaurantWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurante Central";
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var restaurant = new Restaurants(uuid, name, restaurantsType);

    Assertions.assertEquals(uuid, restaurant.getUuid());
    Assertions.assertEquals(name, restaurant.getName());
    Assertions.assertEquals(restaurantsType, restaurant.getRestaurantsType());
  }

  @Test
  @DisplayName("Deve criar um restaurante sem UUID explícito e dados válidos")
  void shouldCreateRestaurantWithoutExplicitUuid() {
    final var name = "Restaurante Central";
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var restaurant = new Restaurants(name, restaurantsType);

    Assertions.assertNotNull(restaurant.getUuid());
    Assertions.assertEquals(name, restaurant.getName());
    Assertions.assertEquals(restaurantsType, restaurant.getRestaurantsType());
  }

  @Test
  @DisplayName("Não deve criar um restaurante sem name e lançar BadRequestException")
  void shouldNotCreateRestaurantWithoutNameAndThrowBadRequestException() {
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurants(UUID.randomUUID(), null, restaurantsType)
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um restaurante com name em branco e lançar BadRequestException")
  void shouldNotCreateRestaurantWithNameBlankAndThrowBadRequestException() {
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurants(UUID.randomUUID(), "   ", restaurantsType)
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um restaurante sem restaurantsType e lançar BadRequestException")
  void shouldNotCreateRestaurantWithoutRestaurantsTypeAndThrowBadRequestException() {
    final var name = "Restaurante Central";

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurants(UUID.randomUUID(), name, null)
    );

    Assertions.assertEquals("Restaurant type is required", exception.getMessage());
  }
}
