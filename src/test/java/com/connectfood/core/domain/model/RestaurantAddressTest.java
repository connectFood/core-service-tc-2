package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantAddressTest {

  @Test
  @DisplayName("Deve criar RestaurantsAddress com UUID explícito e dados válidos")
  void shouldCreateRestaurantsAddressWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final Address address = Mockito.mock(Address.class);

    final var restaurantsAddress = new RestaurantAddress(uuid, restaurant, address);

    Assertions.assertEquals(uuid, restaurantsAddress.getUuid());
    Assertions.assertEquals(restaurant, restaurantsAddress.getRestaurant());
    Assertions.assertEquals(address, restaurantsAddress.getAddress());
  }

  @Test
  @DisplayName("Deve criar RestaurantsAddress sem UUID explícito e gerar UUID automaticamente")
  void shouldCreateRestaurantsAddressWithoutExplicitUuid() {
    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final Address address = Mockito.mock(Address.class);

    final var restaurantsAddress = new RestaurantAddress(restaurant, address);

    Assertions.assertNotNull(restaurantsAddress.getUuid());
    Assertions.assertEquals(restaurant, restaurantsAddress.getRestaurant());
    Assertions.assertEquals(address, restaurantsAddress.getAddress());
  }

  @Test
  @DisplayName("Não deve criar RestaurantsAddress sem Restaurants e deve lançar BadRequestException")
  void shouldNotCreateRestaurantsAddressWithoutRestaurantsAndThrowBadRequestException() {
    final Address address = Mockito.mock(Address.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantAddress(null, address)
    );

    Assertions.assertEquals("Restaurant is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar RestaurantsAddress sem Address e deve lançar BadRequestException")
  void shouldNotCreateRestaurantsAddressWithoutAddressAndThrowBadRequestException() {
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantAddress(restaurant, null)
    );

    Assertions.assertEquals("Address is required", exception.getMessage());
  }
}
