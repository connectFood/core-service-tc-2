package com.connectfood.core.application.restaurant.mapper;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourInput;
import com.connectfood.core.application.restaurant.dto.RestaurantOpeningHourOutput;
import com.connectfood.core.domain.model.RestaurantOpeningHour;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHourAppMapperTest {

  private final RestaurantOpeningHourAppMapper mapper = new RestaurantOpeningHourAppMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new RestaurantOpeningHourAppMapper();
    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("toDomain deve retornar null quando input for null")
  void toDomainShouldReturnNullWhenInputIsNull() {
    final var result = mapper.toDomain(null);
    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("toDomain deve mapear input para domain e gerar uuid quando não informado")
  void toDomainShouldMapInputToDomainAndGenerateUuid() {
    final var input = new RestaurantOpeningHourInput(
        DayOfWeek.MONDAY,
        LocalTime.of(9, 0),
        LocalTime.of(18, 0)
    );

    final RestaurantOpeningHour result = mapper.toDomain(input);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals(DayOfWeek.MONDAY, result.getDayWeek());
    Assertions.assertEquals(LocalTime.of(9, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(18, 0), result.getEndTime());
  }

  @Test
  @DisplayName("toDomain(uuid,input) deve retornar null quando input for null")
  void toDomainWithUuidShouldReturnNullWhenInputIsNull() {
    final var result = mapper.toDomain(UUID.randomUUID(), null);
    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("toDomain(uuid,input) deve mapear input para domain com uuid explícito")
  void toDomainWithUuidShouldMapInputToDomainWithExplicitUuid() {
    final var uuid = UUID.randomUUID();

    final var input = new RestaurantOpeningHourInput(
        DayOfWeek.FRIDAY,
        LocalTime.of(10, 0),
        LocalTime.of(22, 0)
    );

    final RestaurantOpeningHour result = mapper.toDomain(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.FRIDAY, result.getDayWeek());
    Assertions.assertEquals(LocalTime.of(10, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(22, 0), result.getEndTime());
  }

  @Test
  @DisplayName("toOutput deve retornar null quando model for null")
  void toOutputShouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);
    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("toOutput deve mapear model para output corretamente")
  void toOutputShouldMapModelToOutputCorrectly() {
    final var uuid = UUID.randomUUID();

    final var model = new RestaurantOpeningHour(
        uuid,
        DayOfWeek.TUESDAY,
        LocalTime.of(8, 0),
        LocalTime.of(17, 0)
    );

    final RestaurantOpeningHourOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.TUESDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(8, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(17, 0), result.getEndTime());
  }
}
