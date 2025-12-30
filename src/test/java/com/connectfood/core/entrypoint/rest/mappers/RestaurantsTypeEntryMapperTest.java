package com.connectfood.core.entrypoint.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantsTypeEntryMapperTest {

  private final RestaurantsTypeEntryMapper mapper = new RestaurantsTypeEntryMapper();

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantsTypeInput result = mapper.toInput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido")
  void shouldConvertToInputWhenRequestIsProvided() {
    final RestaurantsTypeRequest request =
        new RestaurantsTypeRequest("Fast Food", "Quick meals");

    final RestaurantsTypeInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Fast Food", result.getName());
    Assertions.assertEquals("Quick meals", result.getDescription());
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantsTypeResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para response quando output estiver preenchido")
  void shouldConvertToResponseWhenOutputIsProvided() {
    final var uuid = UUID.randomUUID();

    final RestaurantsTypeOutput output =
        new RestaurantsTypeOutput(uuid, "Italian", "Pasta and pizza");

    final RestaurantsTypeResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Italian", result.getName());
    Assertions.assertEquals("Pasta and pizza", result.getDescription());
  }
}
