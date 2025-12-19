package com.connectfood.core.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsTest {

  @Test
  @DisplayName("Deve criar um item de restaurante com UUID explícito e dados válidos")
  void shouldCreateRestaurantItemsWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = "DELIVERY";
    final Restaurants restaurant = org.mockito.Mockito.mock(Restaurants.class);

    final var item = new RestaurantItems(uuid, name, description, value, requestType, restaurant);

    Assertions.assertEquals(uuid, item.getUuid());
    Assertions.assertEquals(name, item.getName());
    Assertions.assertEquals(description, item.getDescription());
    Assertions.assertEquals(value, item.getValue());
    Assertions.assertEquals(requestType, item.getRequestType());
    Assertions.assertEquals(restaurant, item.getRestaurant());
  }

  @Test
  @DisplayName("Deve criar um item de restaurante sem UUID explícito e dados válidos")
  void shouldCreateRestaurantItemsWithoutExplicitUuid() {
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = "DELIVERY";
    final Restaurants restaurant = org.mockito.Mockito.mock(Restaurants.class);

    final var item = new RestaurantItems(name, description, value, requestType, restaurant);

    Assertions.assertNotNull(item.getUuid());
    Assertions.assertEquals(name, item.getName());
    Assertions.assertEquals(description, item.getDescription());
    Assertions.assertEquals(value, item.getValue());
    Assertions.assertEquals(requestType, item.getRequestType());
    Assertions.assertEquals(restaurant, item.getRestaurant());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante sem nome e lançar uma BadRequest")
  void shouldNotCreateRestaurantItemsWithoutNameAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = "DELIVERY";
    final Restaurants restaurant = org.mockito.Mockito.mock(Restaurants.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItems(uuid, null, description, value, requestType, restaurant)
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante com nome em branco e lançar uma BadRequest")
  void shouldNotCreateRestaurantItemsWithNameIsBlankAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = "DELIVERY";
    final Restaurants restaurant = org.mockito.Mockito.mock(Restaurants.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItems(uuid, "", description, value, requestType, restaurant)
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante sem value e lançar uma BadRequest")
  void shouldNotCreateRestaurantItemsWithoutValueAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var requestType = "DELIVERY";
    final Restaurants restaurant = org.mockito.Mockito.mock(Restaurants.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItems(uuid, name, description, null, requestType, restaurant)
    );

    Assertions.assertEquals("Value is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante sem requestType e lançar uma BadRequest")
  void shouldNotCreateRestaurantItemsWithoutRequestTypeAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final Restaurants restaurant = org.mockito.Mockito.mock(Restaurants.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItems(uuid, name, description, value, null, restaurant)
    );

    Assertions.assertEquals("Request Type hash is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante com requestType em branco e lançar uma BadRequest")
  void shouldNotCreateRestaurantItemsWithRequestTypeIsBlankAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final Restaurants restaurant = org.mockito.Mockito.mock(Restaurants.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItems(uuid, name, description, value, "", restaurant)
    );

    Assertions.assertEquals("Request Type hash is required", exception.getMessage());
  }
}
