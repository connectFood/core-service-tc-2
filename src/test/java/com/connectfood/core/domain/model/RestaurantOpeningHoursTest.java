package com.connectfood.core.domain.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantOpeningHoursTest {

  @Test
  @DisplayName("Deve criar um horário de funcionamento com UUID explícito e dados válidos")
  void shouldCreateRestaurantOpeningHoursWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var dayWeek = DayOfWeek.MONDAY;
    final var startTime = LocalTime.of(9, 0);
    final var endTime = LocalTime.of(18, 0);
    final Restaurants restaurant = Mockito.mock(Restaurants.class);

    final var openingHours = new RestaurantOpeningHours(uuid, dayWeek, startTime, endTime, restaurant);

    Assertions.assertEquals(uuid, openingHours.getUuid());
    Assertions.assertEquals(dayWeek, openingHours.getDayWeek());
    Assertions.assertEquals(startTime, openingHours.getStartTime());
    Assertions.assertEquals(endTime, openingHours.getEndTime());
    Assertions.assertEquals(restaurant, openingHours.getRestaurant());
  }

  @Test
  @DisplayName("Deve criar um horário de funcionamento sem UUID explícito e dados válidos")
  void shouldCreateRestaurantOpeningHoursWithoutExplicitUuid() {
    final var dayWeek = DayOfWeek.MONDAY;
    final var startTime = LocalTime.of(9, 0);
    final var endTime = LocalTime.of(18, 0);
    final Restaurants restaurant = Mockito.mock(Restaurants.class);

    final var openingHours = new RestaurantOpeningHours(dayWeek, startTime, endTime, restaurant);

    Assertions.assertNotNull(openingHours.getUuid());
    Assertions.assertEquals(dayWeek, openingHours.getDayWeek());
    Assertions.assertEquals(startTime, openingHours.getStartTime());
    Assertions.assertEquals(endTime, openingHours.getEndTime());
    Assertions.assertEquals(restaurant, openingHours.getRestaurant());
  }

  @Test
  @DisplayName("Deve criar um horário de funcionamento com construtor que não recebe restaurant")
  void shouldCreateRestaurantOpeningHoursWithoutRestaurant() {
    final var uuid = UUID.randomUUID();
    final var dayWeek = DayOfWeek.SATURDAY;
    final var startTime = LocalTime.of(10, 0);
    final var endTime = LocalTime.of(22, 0);

    final var openingHours = new RestaurantOpeningHours(uuid, dayWeek, startTime, endTime);

    Assertions.assertEquals(uuid, openingHours.getUuid());
    Assertions.assertEquals(dayWeek, openingHours.getDayWeek());
    Assertions.assertEquals(startTime, openingHours.getStartTime());
    Assertions.assertEquals(endTime, openingHours.getEndTime());
    Assertions.assertNull(openingHours.getRestaurant());
  }

  @Test
  @DisplayName("Não deve criar um horário de funcionamento sem dayWeek e lançar BadRequestException")
  void shouldNotCreateRestaurantOpeningHoursWithoutDayWeekAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantOpeningHours(
            UUID.randomUUID(),
            null,
            LocalTime.of(9, 0),
            LocalTime.of(18, 0),
            Mockito.mock(Restaurants.class)
        )
    );

    Assertions.assertEquals("Day of week is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um horário de funcionamento sem startTime e lançar BadRequestException")
  void shouldNotCreateRestaurantOpeningHoursWithoutStartTimeAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantOpeningHours(
            UUID.randomUUID(),
            DayOfWeek.MONDAY,
            null,
            LocalTime.of(18, 0),
            Mockito.mock(Restaurants.class)
        )
    );

    Assertions.assertEquals("Start time is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um horário de funcionamento sem endTime e lançar BadRequestException")
  void shouldNotCreateRestaurantOpeningHoursWithoutEndTimeAndThrowBadRequestException() {
    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new RestaurantOpeningHours(
            UUID.randomUUID(),
            DayOfWeek.MONDAY,
            LocalTime.of(9, 0),
            null,
            Mockito.mock(Restaurants.class)
        )
    );

    Assertions.assertEquals("End time is required", exception.getMessage());
  }
}
