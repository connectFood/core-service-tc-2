package com.connectfood.core.application.restaurantopeninghours.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHoursOutputTest {

  @Test
  @DisplayName("Deve criar output com todos os campos preenchidos")
  void shouldCreateOutputWithAllFields() {
    final var uuid = UUID.randomUUID();
    final var dayOfWeek = DayOfWeek.MONDAY;
    final var startTime = LocalTime.of(9, 0);
    final var endTime = LocalTime.of(18, 0);
    final RestaurantsOutput restaurant = org.mockito.Mockito.mock(RestaurantsOutput.class);

    final var output = new RestaurantOpeningHoursOutput(
        uuid,
        dayOfWeek,
        startTime,
        endTime,
        restaurant
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(dayOfWeek, output.getDayOfWeek());
    Assertions.assertEquals(startTime, output.getStartTime());
    Assertions.assertEquals(endTime, output.getEndTime());
    Assertions.assertEquals(restaurant, output.getRestaurant());
  }

  @Test
  @DisplayName("Deve criar output sem restaurant quando construtor simplificado for usado")
  void shouldCreateOutputWithoutRestaurantUsingSimplifiedConstructor() {
    final var uuid = UUID.randomUUID();
    final var dayOfWeek = DayOfWeek.FRIDAY;
    final var startTime = LocalTime.of(10, 0);
    final var endTime = LocalTime.of(22, 0);

    final var output = new RestaurantOpeningHoursOutput(
        uuid,
        dayOfWeek,
        startTime,
        endTime
    );

    Assertions.assertEquals(uuid, output.getUuid());
    Assertions.assertEquals(dayOfWeek, output.getDayOfWeek());
    Assertions.assertEquals(startTime, output.getStartTime());
    Assertions.assertEquals(endTime, output.getEndTime());
    Assertions.assertNull(output.getRestaurant());
  }

  @Test
  @DisplayName("Deve criar output mesmo com campos nulos")
  void shouldCreateOutputWithNullFields() {
    final var output = new RestaurantOpeningHoursOutput(
        null,
        null,
        null,
        null,
        null
    );

    Assertions.assertNull(output.getUuid());
    Assertions.assertNull(output.getDayOfWeek());
    Assertions.assertNull(output.getStartTime());
    Assertions.assertNull(output.getEndTime());
    Assertions.assertNull(output.getRestaurant());
  }
}
