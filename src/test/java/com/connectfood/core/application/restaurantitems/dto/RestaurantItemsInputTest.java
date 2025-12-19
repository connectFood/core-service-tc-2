package com.connectfood.core.application.restaurantitems.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsInputTest {

  @Test
  @DisplayName("Deve criar RestaurantItemsInput com todos os campos válidos")
  void shouldCreateRestaurantItemsInputWithValidData() {
    final var name = "Pizza Margherita";
    final var description = "Pizza tradicional italiana";
    final var value = BigDecimal.valueOf(49.90);
    final var requestType = RestaurantItemServiceType.DELIVERY;
    final var restaurantUuid = UUID.randomUUID();

    final var input = new RestaurantItemsInput(
        name,
        description,
        value,
        requestType,
        restaurantUuid
    );

    Assertions.assertEquals(name, input.getName());
    Assertions.assertEquals(description, input.getDescription());
    Assertions.assertEquals(value, input.getValue());
    Assertions.assertEquals(requestType, input.getRequestType());
    Assertions.assertEquals(restaurantUuid, input.getRestaurantUuid());
  }

  @Test
  @DisplayName("Deve permitir criar RestaurantItemsInput com campos opcionais nulos")
  void shouldCreateRestaurantItemsInputWithNullOptionalFields() {
    final var name = "Pizza";
    final BigDecimal value = BigDecimal.valueOf(30.00);
    final var requestType = RestaurantItemServiceType.LOCAL_ONLY;
    final var restaurantUuid = UUID.randomUUID();

    final var input = new RestaurantItemsInput(
        name,
        null,
        value,
        requestType,
        restaurantUuid
    );

    Assertions.assertEquals(name, input.getName());
    Assertions.assertNull(input.getDescription());
    Assertions.assertEquals(value, input.getValue());
    Assertions.assertEquals(requestType, input.getRequestType());
    Assertions.assertEquals(restaurantUuid, input.getRestaurantUuid());
  }
}
