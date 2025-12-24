package com.connectfood.core.application.restaurantitems.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsOutputTest {

  @Test
  @DisplayName("Deve criar RestaurantItemsOutput com restaurante associado")
  void shouldCreateRestaurantItemsOutputWithRestaurant() {
    final var uuid = UUID.randomUUID();
    final var name = "Pizza Margherita";
    final var description = "Pizza tradicional italiana";
    final var value = BigDecimal.valueOf(49.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;

    final RestaurantsOutput restaurant = Mockito.mock(RestaurantsOutput.class);

    final var output = new RestaurantItemsOutput(
        uuid,
        name,
        description,
        value,
        requestType,
        restaurant
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(name, output.getName());
    Assertions.assertEquals(description, output.getDescription());
    Assertions.assertEquals(value, output.getValue());
    Assertions.assertEquals(requestType, output.getRequestType());
    Assertions.assertEquals(restaurant, output.getRestaurant());
  }

  @Test
  @DisplayName("Deve criar RestaurantItemsOutput sem restaurante associado")
  void shouldCreateRestaurantItemsOutputWithoutRestaurant() {
    final var uuid = UUID.randomUUID();
    final var name = "Pizza Calabresa";
    final var description = "Pizza com calabresa";
    final var value = BigDecimal.valueOf(45.00);
    final var requestType = RestaurantItemServiceType.LOCAL_ONLY;

    final var output = new RestaurantItemsOutput(
        uuid,
        name,
        description,
        value,
        requestType
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(name, output.getName());
    Assertions.assertEquals(description, output.getDescription());
    Assertions.assertEquals(value, output.getValue());
    Assertions.assertEquals(requestType, output.getRequestType());
    Assertions.assertNull(output.getRestaurant());
  }
}
