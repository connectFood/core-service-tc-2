package com.connectfood.infrastructure.persistence.entity;

import java.time.DayOfWeek;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHourEntityTest {

  @Test
  @DisplayName("Deve definir e obter o dia da semana corretamente")
  void shouldSetAndGetDayOfWeekCorrectly() {
    final var entity = new RestaurantOpeningHourEntity();

    entity.setDayWeek(DayOfWeek.MONDAY);

    Assertions.assertEquals(DayOfWeek.MONDAY.getValue(), entity.getDayWeek());
    Assertions.assertEquals(DayOfWeek.MONDAY, entity.getDayOfWeek());
  }
}
