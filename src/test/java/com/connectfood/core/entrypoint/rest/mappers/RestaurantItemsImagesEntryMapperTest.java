package com.connectfood.core.entrypoint.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsImagesRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsImagesResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantItemsImagesEntryMapperTest {

  private final RestaurantItemsImagesEntryMapper mapper =
      new RestaurantItemsImagesEntryMapper();

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantItemsImagesInput result = mapper.toInput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido sem uuid")
  void shouldConvertToInputWhenRequestIsProvidedWithoutUuid() {
    final RestaurantItemsImagesRequest request =
        new RestaurantItemsImagesRequest(
            null,
            "IMAGE",
            "Image description",
            "/path/image.png"
        );

    final RestaurantItemsImagesInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertNull(result.getUuid());
    Assertions.assertEquals("IMAGE", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/path/image.png", result.getPath());
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido com uuid")
  void shouldConvertToInputWhenRequestIsProvidedWithUuid() {
    final var uuid = UUID.randomUUID();

    final RestaurantItemsImagesRequest request =
        new RestaurantItemsImagesRequest(
            uuid,
            "IMAGE",
            "Image description",
            "/path/image.png"
        );

    final RestaurantItemsImagesInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/path/image.png", result.getPath());
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantItemsImagesResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para response quando output estiver preenchido")
  void shouldConvertToResponseWhenOutputIsProvided() {
    final var uuid = UUID.randomUUID();

    final RestaurantItemsImagesOutput output =
        new RestaurantItemsImagesOutput(
            uuid,
            "IMAGE",
            "Image description",
            "/path/image.png"
        );

    final RestaurantItemsImagesResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/path/image.png", result.getPath());
  }
}
