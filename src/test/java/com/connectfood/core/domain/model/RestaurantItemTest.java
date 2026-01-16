package com.connectfood.core.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemTest {

  @Test
  @DisplayName("Deve criar um item de restaurante com UUID explícito e dados válidos")
  void shouldCreateRestaurantItemsWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var item = new RestaurantItem(uuid, name, description, value, requestType, restaurant);

    Assertions.assertEquals(uuid, item.getUuid());
    Assertions.assertEquals(name, item.getName());
    Assertions.assertEquals(description, item.getDescription());
    Assertions.assertEquals(value, item.getValue());
    Assertions.assertEquals(requestType, item.getRequestType());
    Assertions.assertEquals(restaurant, item.getRestaurant());
    Assertions.assertNull(item.getImages());
  }

  @Test
  @DisplayName("Deve criar um item de restaurante sem UUID explícito e dados válidos")
  void shouldCreateRestaurantItemsWithoutExplicitUuid() {
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var item = new RestaurantItem(name, description, value, requestType, restaurant);

    Assertions.assertNotNull(item.getUuid());
    Assertions.assertEquals(name, item.getName());
    Assertions.assertEquals(description, item.getDescription());
    Assertions.assertEquals(value, item.getValue());
    Assertions.assertEquals(requestType, item.getRequestType());
    Assertions.assertEquals(restaurant, item.getRestaurant());
    Assertions.assertNull(item.getImages());
  }

  @Test
  @DisplayName("Deve criar um item com construtor completo incluindo imagens")
  void shouldCreateRestaurantItemsWithImages() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final List<RestaurantItemImage> images = List.of(Mockito.mock(RestaurantItemImage.class));

    final var item = new RestaurantItem(uuid, name, description, value, requestType, restaurant, images);

    Assertions.assertEquals(uuid, item.getUuid());
    Assertions.assertEquals(name, item.getName());
    Assertions.assertEquals(description, item.getDescription());
    Assertions.assertEquals(value, item.getValue());
    Assertions.assertEquals(requestType, item.getRequestType());
    Assertions.assertEquals(restaurant, item.getRestaurant());
    Assertions.assertEquals(images, item.getImages());
  }

  @Test
  @DisplayName("Deve criar um item com constructor que não recebe restaurant nem images")
  void shouldCreateRestaurantItemsWithoutRestaurantAndImages() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;

    final var item = new RestaurantItem(uuid, name, description, value, requestType);

    Assertions.assertEquals(uuid, item.getUuid());
    Assertions.assertEquals(name, item.getName());
    Assertions.assertEquals(description, item.getDescription());
    Assertions.assertEquals(value, item.getValue());
    Assertions.assertEquals(requestType, item.getRequestType());
    Assertions.assertNull(item.getRestaurant());
    Assertions.assertNull(item.getImages());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante sem nome e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsWithoutNameAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItem(uuid, null, description, value, requestType, restaurant)
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante com nome vazio e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsWithNameEmptyAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItem(uuid, "", description, value, requestType, restaurant)
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante com nome apenas espaços e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsWithNameBlankSpacesAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItem(uuid, "   ", description, value, requestType, restaurant)
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante sem value e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsWithoutValueAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItem(uuid, name, description, null, requestType, restaurant)
    );

    Assertions.assertEquals("Value is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um item de restaurante sem requestType e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsWithoutRequestTypeAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST ITEM";
    final var description = "Test item description";
    final var value = BigDecimal.valueOf(19.90);
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItem(uuid, name, description, value, null, restaurant)
    );

    Assertions.assertEquals("Request Type hash is required", exception.getMessage());
  }
}
