package com.connectfood.core.application.restaurantitems.dto;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantItemsImagesInputTest {

  @Test
  @DisplayName("Deve criar RestaurantItemsImagesInput com todos os campos preenchidos")
  void shouldCreateRestaurantItemsImagesInputWithAllFields() {
    final var uuid = UUID.randomUUID();
    final var name = "image.png";
    final var description = "Main image";
    final var path = "/images/image.png";

    final var input = new RestaurantItemsImagesInput(
        uuid,
        name,
        description,
        path
    );

    Assertions.assertEquals(uuid, input.getUuid());
    Assertions.assertEquals(name, input.getName());
    Assertions.assertEquals(description, input.getDescription());
    Assertions.assertEquals(path, input.getPath());
  }

  @Test
  @DisplayName("Deve criar RestaurantItemsImagesInput mesmo com campos nulos")
  void shouldCreateRestaurantItemsImagesInputWithNullFields() {
    final var input = new RestaurantItemsImagesInput(
        null,
        null,
        null,
        null
    );

    Assertions.assertNull(input.getUuid());
    Assertions.assertNull(input.getName());
    Assertions.assertNull(input.getDescription());
    Assertions.assertNull(input.getPath());
  }
}
