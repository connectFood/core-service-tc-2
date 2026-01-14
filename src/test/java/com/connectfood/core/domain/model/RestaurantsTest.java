package com.connectfood.core.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
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

    Assertions.assertNull(restaurant.getOpeningHours());
    Assertions.assertNull(restaurant.getAddress());
    Assertions.assertNull(restaurant.getUsers());
  }

  @Test
  @DisplayName("Deve criar um restaurante sem UUID explícito e gerar UUID automaticamente")
  void shouldCreateRestaurantWithoutExplicitUuid() {
    final var name = "Restaurante Central";
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var restaurant = new Restaurants(name, restaurantsType);

    Assertions.assertNotNull(restaurant.getUuid());
    Assertions.assertEquals(name, restaurant.getName());
    Assertions.assertEquals(restaurantsType, restaurant.getRestaurantsType());

    Assertions.assertNull(restaurant.getOpeningHours());
    Assertions.assertNull(restaurant.getAddress());
    Assertions.assertNull(restaurant.getUsers());
  }

  @Test
  @DisplayName("Deve criar um restaurante usando o construtor completo com dados opcionais")
  void shouldCreateRestaurantWithFullConstructor() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurante Central";
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var openingHours = List.of(
        new RestaurantOpeningHours(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)),
        new RestaurantOpeningHours(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(22, 0))
    );

    final Address address = Mockito.mock(Address.class);
    final Users users = Mockito.mock(Users.class);

    final var restaurant = new Restaurants(uuid, name, restaurantsType, openingHours, address, users);

    Assertions.assertEquals(uuid, restaurant.getUuid());
    Assertions.assertEquals(name, restaurant.getName());
    Assertions.assertEquals(restaurantsType, restaurant.getRestaurantsType());
    Assertions.assertEquals(openingHours, restaurant.getOpeningHours());
    Assertions.assertEquals(address, restaurant.getAddress());
    Assertions.assertEquals(users, restaurant.getUsers());
  }

  @Test
  @DisplayName("Não deve criar um restaurante sem name e deve lançar BadRequestException")
  void shouldNotCreateRestaurantWithoutNameAndThrowBadRequestException() {
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurants(UUID.randomUUID(), null, restaurantsType)
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um restaurante com name em branco e deve lançar BadRequestException")
  void shouldNotCreateRestaurantWithNameBlankAndThrowBadRequestException() {
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurants(UUID.randomUUID(), "   ", restaurantsType)
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um restaurante sem restaurantsType e deve lançar BadRequestException")
  void shouldNotCreateRestaurantWithoutRestaurantsTypeAndThrowBadRequestException() {
    final var name = "Restaurante Central";

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurants(UUID.randomUUID(), name, null)
    );

    Assertions.assertEquals("Restaurant type is required", exception.getMessage());
  }
}
