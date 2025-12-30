package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantItemsImagesTest {

  @Test
  @DisplayName("Deve criar uma imagem de item com UUID explícito e dados válidos")
  void shouldCreateRestaurantItemsImagesWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var name = "IMAGE_01";
    final var description = "Image description";
    final var path = "https://cdn.example.com/items/image-01.png";

    final var image = new RestaurantItemsImages(uuid, name, description, path);

    Assertions.assertEquals(uuid, image.getUuid());
    Assertions.assertEquals(name, image.getName());
    Assertions.assertEquals(description, image.getDescription());
    Assertions.assertEquals(path, image.getPath());
  }

  @Test
  @DisplayName("Deve criar uma imagem de item sem UUID explícito e dados válidos")
  void shouldCreateRestaurantItemsImagesWithoutExplicitUuid() {
    final var name = "IMAGE_01";
    final var description = "Image description";
    final var path = "https://cdn.example.com/items/image-01.png";

    final var image = new RestaurantItemsImages(name, description, path);

    Assertions.assertNotNull(image.getUuid());
    Assertions.assertEquals(name, image.getName());
    Assertions.assertEquals(description, image.getDescription());
    Assertions.assertEquals(path, image.getPath());
  }

  @Test
  @DisplayName("Não deve criar uma imagem de item sem name e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsImagesWithoutNameAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItemsImages(UUID.randomUUID(), null, "Image description", "/items/image.png")
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar uma imagem de item com name em branco e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsImagesWithNameBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItemsImages(UUID.randomUUID(), "   ", "Image description", "/items/image.png")
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar uma imagem de item sem path e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsImagesWithoutPathAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItemsImages(UUID.randomUUID(), "IMAGE_01", "Image description", null)
    );

    Assertions.assertEquals("Path hash is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar uma imagem de item com path em branco e lançar BadRequestException")
  void shouldNotCreateRestaurantItemsImagesWithPathBlankAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantItemsImages(UUID.randomUUID(), "IMAGE_01", "Image description", "")
    );

    Assertions.assertEquals("Path hash is required", exception.getMessage());
  }
}
