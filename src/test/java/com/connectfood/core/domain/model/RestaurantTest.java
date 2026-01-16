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

class RestaurantTest {

  @Test
  @DisplayName("Deve criar um restaurante com UUID explícito e dados válidos")
  void shouldCreateRestaurantWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurante Central";
    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var restaurant = new Restaurant(uuid, name, restaurantType);

    Assertions.assertEquals(uuid, restaurant.getUuid());
    Assertions.assertEquals(name, restaurant.getName());
    Assertions.assertEquals(restaurantType, restaurant.getRestaurantType());

    Assertions.assertNull(restaurant.getOpeningHours());
    Assertions.assertNull(restaurant.getAddress());
    Assertions.assertNull(restaurant.getUser());
  }

  @Test
  @DisplayName("Deve criar um restaurante sem UUID explícito e gerar UUID automaticamente")
  void shouldCreateRestaurantWithoutExplicitUuid() {
    final var name = "Restaurante Central";
    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var restaurant = new Restaurant(name, restaurantType);

    Assertions.assertNotNull(restaurant.getUuid());
    Assertions.assertEquals(name, restaurant.getName());
    Assertions.assertEquals(restaurantType, restaurant.getRestaurantType());

    Assertions.assertNull(restaurant.getOpeningHours());
    Assertions.assertNull(restaurant.getAddress());
    Assertions.assertNull(restaurant.getUser());
  }

  @Test
  @DisplayName("Deve criar um restaurante usando o construtor completo com dados opcionais")
  void shouldCreateRestaurantWithFullConstructor() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurante Central";
    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var openingHours = List.of(
        new RestaurantOpeningHour(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)),
        new RestaurantOpeningHour(DayOfWeek.SATURDAY, LocalTime.of(10, 0), LocalTime.of(22, 0))
    );

    final Address address = Mockito.mock(Address.class);
    final User user = Mockito.mock(User.class);

    final var restaurant = new Restaurant(uuid, name, restaurantType, openingHours, address, user);

    Assertions.assertEquals(uuid, restaurant.getUuid());
    Assertions.assertEquals(name, restaurant.getName());
    Assertions.assertEquals(restaurantType, restaurant.getRestaurantType());
    Assertions.assertEquals(openingHours, restaurant.getOpeningHours());
    Assertions.assertEquals(address, restaurant.getAddress());
    Assertions.assertEquals(user, restaurant.getUser());
  }

  @Test
  @DisplayName("Não deve criar um restaurante sem name e deve lançar BadRequestException")
  void shouldNotCreateRestaurantWithoutNameAndThrowBadRequestException() {
    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurant(UUID.randomUUID(), null, restaurantType)
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um restaurante com name em branco e deve lançar BadRequestException")
  void shouldNotCreateRestaurantWithNameBlankAndThrowBadRequestException() {
    final RestaurantType restaurantType = Mockito.mock(RestaurantType.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurant(UUID.randomUUID(), "   ", restaurantType)
    );

    Assertions.assertEquals("Name cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um restaurante sem restaurantsType e deve lançar BadRequestException")
  void shouldNotCreateRestaurantWithoutRestaurantsTypeAndThrowBadRequestException() {
    final var name = "Restaurante Central";

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new Restaurant(UUID.randomUUID(), name, null)
    );

    Assertions.assertEquals("Restaurant type is required", exception.getMessage());
  }
}
