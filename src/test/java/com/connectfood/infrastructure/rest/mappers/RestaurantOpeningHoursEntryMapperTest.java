package com.connectfood.infrastructure.rest.mappers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.infrastructure.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;
import com.connectfood.infrastructure.rest.mappers.RestaurantOpeningHoursEntryMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHoursEntryMapperTest {

  private final RestaurantOpeningHoursEntryMapper mapper = new RestaurantOpeningHoursEntryMapper();

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantOpeningHoursInput result = mapper.toInput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido")
  void shouldConvertToInputWhenRequestIsProvided() {
    final var request = new RestaurantOpeningHoursRequest(
        DayOfWeek.MONDAY,
        LocalTime.of(9, 0),
        LocalTime.of(18, 0)
    );

    final RestaurantOpeningHoursInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(9, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(18, 0), result.getEndTime());
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantOpeningHoursResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para response quando output estiver preenchido")
  void shouldConvertToResponseWhenOutputIsProvided() {
    final var uuid = UUID.randomUUID();

    final RestaurantOpeningHoursOutput output = new RestaurantOpeningHoursOutput(
        uuid,
        DayOfWeek.FRIDAY,
        LocalTime.of(10, 0),
        LocalTime.of(22, 0)
    );

    final RestaurantOpeningHoursResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.FRIDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(10, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(22, 0), result.getEndTime());
  }
}
