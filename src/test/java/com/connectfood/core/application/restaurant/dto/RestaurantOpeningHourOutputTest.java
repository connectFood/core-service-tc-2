package com.connectfood.core.application.restaurant.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHourOutputTest {

  @Test
  @DisplayName("Deve criar output com todos os campos preenchidos")
  void shouldCreateOutputWithAllFields() {
    final var uuid = UUID.randomUUID();
    final var dayOfWeek = DayOfWeek.MONDAY;
    final var startTime = LocalTime.of(9, 0);
    final var endTime = LocalTime.of(18, 0);

    final var output = new RestaurantOpeningHourOutput(
        uuid,
        dayOfWeek,
        startTime,
        endTime
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(dayOfWeek, output.getDayOfWeek());
    Assertions.assertEquals(startTime, output.getStartTime());
    Assertions.assertEquals(endTime, output.getEndTime());
  }

  @Test
  @DisplayName("Deve criar output mesmo com campos nulos")
  void shouldCreateOutputWithNullFields() {
    final var output = new RestaurantOpeningHourOutput(
        null,
        null,
        null,
        null
    );

    Assertions.assertNull(output.getUuid());
    Assertions.assertNull(output.getDayOfWeek());
    Assertions.assertNull(output.getStartTime());
    Assertions.assertNull(output.getEndTime());
  }
}
