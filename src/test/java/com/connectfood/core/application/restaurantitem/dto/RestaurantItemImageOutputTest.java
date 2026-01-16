package com.connectfood.core.application.restaurantitem.dto;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantItemImageOutputTest {

  @Test
  @DisplayName("Deve criar RestaurantItemsImagesOutput com todos os campos preenchidos")
  void shouldCreateRestaurantItemsImagesOutputWithAllFields() {
    final var uuid = UUID.randomUUID();
    final var name = "image.png";
    final var description = "Main image";
    final var path = "/images/image.png";

    final var output = new RestaurantItemImageOutput(
        uuid,
        name,
        description,
        path
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(name, output.getName());
    Assertions.assertEquals(description, output.getDescription());
    Assertions.assertEquals(path, output.getPath());
  }

  @Test
  @DisplayName("Deve criar RestaurantItemsImagesOutput mesmo com campos nulos")
  void shouldCreateRestaurantItemsImagesOutputWithNullFields() {
    final var output = new RestaurantItemImageOutput(
        null,
        null,
        null,
        null
    );

    Assertions.assertNull(output.getUuid());
    Assertions.assertNull(output.getName());
    Assertions.assertNull(output.getDescription());
    Assertions.assertNull(output.getPath());
  }
}
