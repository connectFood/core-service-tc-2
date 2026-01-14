package com.connectfood.core.application.restaurants.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHoursInputTest {

  @Test
  @DisplayName("Deve criar input com todos os campos preenchidos")
  void shouldCreateInputWithAllFields() {
    final var dayOfWeek = DayOfWeek.MONDAY;
    final var startTime = LocalTime.of(9, 0);
    final var endTime = LocalTime.of(18, 0);

    final var input = new RestaurantOpeningHoursInput(
        dayOfWeek,
        startTime,
        endTime
    );

    Assertions.assertEquals(dayOfWeek, input.getDayOfWeek());
    Assertions.assertEquals(startTime, input.getStartTime());
    Assertions.assertEquals(endTime, input.getEndTime());
  }

  @Test
  @DisplayName("Deve criar input mesmo com campos nulos")
  void shouldCreateInputWithNullFields() {
    final var input = new RestaurantOpeningHoursInput(
        null,
        null,
        null
    );

    Assertions.assertNull(input.getDayOfWeek());
    Assertions.assertNull(input.getStartTime());
    Assertions.assertNull(input.getEndTime());
  }
}
