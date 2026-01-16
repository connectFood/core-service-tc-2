package com.connectfood.infrastructure.persistence.mappers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.infrastructure.persistence.mappers.RestaurantOpeningHoursInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantOpeningHoursInfraMapperTest {

  private final RestaurantOpeningHoursInfraMapper mapper = new RestaurantOpeningHoursInfraMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new RestaurantOpeningHoursInfraMapper();
    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final var result = mapper.toDomain(null);
    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear entity para domain corretamente")
  void toDomainShouldMapEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();
    final var day = DayOfWeek.MONDAY;
    final var start = LocalTime.of(8, 0);
    final var end = LocalTime.of(18, 0);

    final var entity = new RestaurantOpeningHoursEntity();
    entity.setUuid(uuid);
    entity.setDayWeek(day);
    entity.setStartTime(start);
    entity.setEndTime(end);

    final RestaurantOpeningHours result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(day, result.getDayWeek());
    Assertions.assertEquals(start, result.getStartTime());
    Assertions.assertEquals(end, result.getEndTime());
  }

  @Test
  @DisplayName("Deve retornar null quando model for null no toEntity com restaurantsEntity")
  void toEntityShouldReturnNullWhenModelIsNull() {
    final var restaurantsEntity = new RestaurantsEntity();

    final var result = mapper.toEntity(null, restaurantsEntity);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsEntity for null no toEntity")
  void toEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final var model = new RestaurantOpeningHours(
        UUID.randomUUID(),
        DayOfWeek.TUESDAY,
        LocalTime.of(9, 0),
        LocalTime.of(17, 0)
    );

    final var result = mapper.toEntity(model, (RestaurantsEntity) null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear domain para entity corretamente com restaurant informado")
  void toEntityShouldMapDomainToEntityCorrectly() {
    final var uuid = UUID.randomUUID();
    final var day = DayOfWeek.THURSDAY;
    final var start = LocalTime.of(8, 30);
    final var end = LocalTime.of(19, 0);

    final var model = new RestaurantOpeningHours(uuid, day, start, end);
    final var restaurantsEntity = new RestaurantsEntity();

    final var result = mapper.toEntity(model, restaurantsEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(day, result.getDayOfWeek());
    Assertions.assertEquals(start, result.getStartTime());
    Assertions.assertEquals(end, result.getEndTime());
    Assertions.assertSame(restaurantsEntity, result.getRestaurant());
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model (apenas startTime/endTime)")
  void toEntityWithExistingEntityShouldUpdateOnlyTimes() {
    final var originalUuid = UUID.randomUUID();
    final var originalDay = DayOfWeek.SATURDAY;
    final var restaurantsEntity = new RestaurantsEntity();

    final var entity = new RestaurantOpeningHoursEntity();
    entity.setUuid(originalUuid);
    entity.setDayWeek(originalDay);
    entity.setRestaurant(restaurantsEntity);
    entity.setStartTime(LocalTime.of(7, 0));
    entity.setEndTime(LocalTime.of(12, 0));

    final var model = new RestaurantOpeningHours(
        UUID.randomUUID(),
        DayOfWeek.FRIDAY,
        LocalTime.of(9, 0),
        LocalTime.of(18, 0)
    );

    final var result = mapper.toEntity(model, entity);

    Assertions.assertSame(entity, result);

    Assertions.assertEquals(LocalTime.of(9, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(18, 0), result.getEndTime());

    Assertions.assertEquals(originalUuid, result.getUuid());
    Assertions.assertEquals(originalDay, result.getDayOfWeek());
    Assertions.assertSame(restaurantsEntity, result.getRestaurant());
  }
}
