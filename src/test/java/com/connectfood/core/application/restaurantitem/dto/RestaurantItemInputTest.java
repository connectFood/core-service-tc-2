package com.connectfood.core.application.restaurantitem.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemInputTest {

  @Test
  @DisplayName("Deve criar RestaurantItemsInput com todos os campos válidos")
  void shouldCreateRestaurantItemsInputWithValidData() {
    final var name = "Pizza Margherita";
    final var description = "Pizza tradicional italiana";
    final var value = BigDecimal.valueOf(49.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final var restaurantUuid = UUID.randomUUID();

    final var images = List.<RestaurantItemImageInput>of();

    final var input = new RestaurantItemInput(
        name,
        description,
        value,
        requestType,
        restaurantUuid,
        images
    );

    Assertions.assertEquals(name, input.getName());
    Assertions.assertEquals(description, input.getDescription());
    Assertions.assertEquals(value, input.getValue());
    Assertions.assertEquals(requestType, input.getRequestType());
    Assertions.assertEquals(restaurantUuid, input.getRestaurantUuid());
    Assertions.assertEquals(images, input.getImages());
  }

  @Test
  @DisplayName("Deve permitir criar RestaurantItemsInput com campos opcionais nulos")
  void shouldCreateRestaurantItemsInputWithNullOptionalFields() {
    final var name = "Pizza";
    final BigDecimal value = BigDecimal.valueOf(30.00);
    final var requestType = RestaurantItemServiceType.LOCAL_ONLY;
    final var restaurantUuid = UUID.randomUUID();

    final var input = new RestaurantItemInput(
        name,
        null,
        value,
        requestType,
        restaurantUuid,
        null
    );

    Assertions.assertEquals(name, input.getName());
    Assertions.assertNull(input.getDescription());
    Assertions.assertEquals(value, input.getValue());
    Assertions.assertEquals(requestType, input.getRequestType());
    Assertions.assertEquals(restaurantUuid, input.getRestaurantUuid());
    Assertions.assertNull(input.getImages());
  }
}
