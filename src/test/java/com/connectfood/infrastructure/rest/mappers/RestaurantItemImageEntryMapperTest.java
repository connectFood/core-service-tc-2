package com.connectfood.infrastructure.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.restaurantitem.dto.RestaurantItemImageInput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemImageOutput;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemImageRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemImageResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantItemImageEntryMapperTest {

  private final RestaurantItemImageEntryMapper mapper =
      new RestaurantItemImageEntryMapper();

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantItemImageInput result = mapper.toInput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido sem uuid")
  void shouldConvertToInputWhenRequestIsProvidedWithoutUuid() {
    final RestaurantItemImageRequest request =
        new RestaurantItemImageRequest(
            null,
            "IMAGE",
            "Image description",
            "/path/image.png"
        );

    final RestaurantItemImageInput result = mapper.toInput(request);

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

    final RestaurantItemImageRequest request =
        new RestaurantItemImageRequest(
            uuid,
            "IMAGE",
            "Image description",
            "/path/image.png"
        );

    final RestaurantItemImageInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/path/image.png", result.getPath());
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantItemImageResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para response quando output estiver preenchido")
  void shouldConvertToResponseWhenOutputIsProvided() {
    final var uuid = UUID.randomUUID();

    final RestaurantItemImageOutput output =
        new RestaurantItemImageOutput(
            uuid,
            "IMAGE",
            "Image description",
            "/path/image.png"
        );

    final RestaurantItemImageResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("IMAGE", result.getName());
    Assertions.assertEquals("Image description", result.getDescription());
    Assertions.assertEquals("/path/image.png", result.getPath());
  }
}
