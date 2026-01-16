package com.connectfood.core.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHourTest {

  @Test
  @DisplayName("Deve criar um horário de funcionamento com UUID explícito e dados válidos")
  void shouldCreateRestaurantOpeningHoursWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var dayWeek = DayOfWeek.MONDAY;
    final var startTime = LocalTime.of(9, 0);
    final var endTime = LocalTime.of(18, 0);

    final var openingHours = new RestaurantOpeningHour(uuid, dayWeek, startTime, endTime);

    Assertions.assertEquals(uuid, openingHours.getUuid());
    Assertions.assertEquals(dayWeek, openingHours.getDayWeek());
    Assertions.assertEquals(startTime, openingHours.getStartTime());
    Assertions.assertEquals(endTime, openingHours.getEndTime());
  }

  @Test
  @DisplayName("Deve criar um horário de funcionamento sem UUID explícito e gerar UUID automaticamente")
  void shouldCreateRestaurantOpeningHoursWithoutExplicitUuid() {
    final var dayWeek = DayOfWeek.MONDAY;
    final var startTime = LocalTime.of(9, 0);
    final var endTime = LocalTime.of(18, 0);

    final var openingHours = new RestaurantOpeningHour(dayWeek, startTime, endTime);

    Assertions.assertNotNull(openingHours.getUuid());
    Assertions.assertEquals(dayWeek, openingHours.getDayWeek());
    Assertions.assertEquals(startTime, openingHours.getStartTime());
    Assertions.assertEquals(endTime, openingHours.getEndTime());
  }

  @Test
  @DisplayName("Não deve criar um horário de funcionamento sem dayWeek e deve lançar BadRequestException")
  void shouldNotCreateRestaurantOpeningHoursWithoutDayWeekAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantOpeningHour(
            UUID.randomUUID(),
            null,
            LocalTime.of(9, 0),
            LocalTime.of(18, 0)
        )
    );

    Assertions.assertEquals("Day of week is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um horário de funcionamento sem startTime e deve lançar BadRequestException")
  void shouldNotCreateRestaurantOpeningHoursWithoutStartTimeAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantOpeningHour(
            UUID.randomUUID(),
            DayOfWeek.MONDAY,
            null,
            LocalTime.of(18, 0)
        )
    );

    Assertions.assertEquals("Start time is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um horário de funcionamento sem endTime e deve lançar BadRequestException")
  void shouldNotCreateRestaurantOpeningHoursWithoutEndTimeAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantOpeningHour(
            UUID.randomUUID(),
            DayOfWeek.MONDAY,
            LocalTime.of(9, 0),
            null
        )
    );

    Assertions.assertEquals("End time is required", exception.getMessage());
  }
}
