package com.connectfood.infrastructure.rest.mappers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghour.RestaurantOpeningHourResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHourEntryMapperTest {

  private final RestaurantOpeningHourEntryMapper mapper = new RestaurantOpeningHourEntryMapper();

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantOpeningHourInput result = mapper.toInput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido")
  void shouldConvertToInputWhenRequestIsProvided() {
    final var request = new RestaurantOpeningHourRequest(
        DayOfWeek.MONDAY,
        LocalTime.of(9, 0),
        LocalTime.of(18, 0)
    );

    final RestaurantOpeningHourInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(9, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(18, 0), result.getEndTime());
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantOpeningHourResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para response quando output estiver preenchido")
  void shouldConvertToResponseWhenOutputIsProvided() {
    final var uuid = UUID.randomUUID();

    final RestaurantOpeningHourOutput output = new RestaurantOpeningHourOutput(
        uuid,
        DayOfWeek.FRIDAY,
        LocalTime.of(10, 0),
        LocalTime.of(22, 0)
    );

    final RestaurantOpeningHourResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.FRIDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(10, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(22, 0), result.getEndTime());
  }
}
