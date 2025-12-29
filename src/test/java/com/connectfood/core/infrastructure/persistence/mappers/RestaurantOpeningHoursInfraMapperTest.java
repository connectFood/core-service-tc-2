package com.connectfood.core.infrastructure.persistence.mappers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantOpeningHoursInfraMapperTest {

  @Mock
  private RestaurantsInfraMapper restaurantsMapper;

  @InjectMocks
  private RestaurantOpeningHoursInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final var result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain quando restaurant estiver presente")
  void toDomainShouldMapEntityToDomainWhenRestaurantIsPresent() {
    final var uuid = UUID.randomUUID();
    final var dayWeek = DayOfWeek.MONDAY;
    final var start = LocalTime.of(8, 0);
    final var end = LocalTime.of(18, 0);

    final RestaurantsEntity restaurantEntity = Mockito.mock(RestaurantsEntity.class);
    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);

    Mockito.when(restaurantsMapper.toDomain(restaurantEntity))
        .thenReturn(restaurantDomain);

    final var entity = Mockito.mock(RestaurantOpeningHoursEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getDayOfWeek())
        .thenReturn(dayWeek);
    Mockito.when(entity.getStartTime())
        .thenReturn(start);
    Mockito.when(entity.getEndTime())
        .thenReturn(end);
    Mockito.when(entity.getRestaurant())
        .thenReturn(restaurantEntity);

    final RestaurantOpeningHours result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(dayWeek, result.getDayWeek());
    Assertions.assertEquals(start, result.getStartTime());
    Assertions.assertEquals(end, result.getEndTime());
    Assertions.assertSame(restaurantDomain, result.getRestaurant());

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toDomain(restaurantEntity);
  }

  @Test
  @DisplayName("Deve mapear entity para domain quando restaurant for null")
  void toDomainShouldMapEntityToDomainWhenRestaurantIsNull() {
    final var uuid = UUID.randomUUID();

    final var entity = Mockito.mock(RestaurantOpeningHoursEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getDayOfWeek())
        .thenReturn(DayOfWeek.TUESDAY);
    Mockito.when(entity.getStartTime())
        .thenReturn(LocalTime.of(9, 0));
    Mockito.when(entity.getEndTime())
        .thenReturn(LocalTime.of(17, 0));
    Mockito.when(entity.getRestaurant())
        .thenReturn(null);

    final var result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertNull(result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve retornar null no toDomainAll quando entity for null")
  void toDomainAllShouldReturnNullWhenEntityIsNull() {
    final var result = mapper.toDomainAll(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain no toDomainAll sem restaurant")
  void toDomainAllShouldMapEntityToDomainWithoutRestaurant() {
    final var uuid = UUID.randomUUID();

    final var entity = Mockito.mock(RestaurantOpeningHoursEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getDayOfWeek())
        .thenReturn(DayOfWeek.WEDNESDAY);
    Mockito.when(entity.getStartTime())
        .thenReturn(LocalTime.of(10, 0));
    Mockito.when(entity.getEndTime())
        .thenReturn(LocalTime.of(20, 0));

    final var result = mapper.toDomainAll(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.WEDNESDAY, result.getDayWeek());
    Assertions.assertEquals(LocalTime.of(10, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(20, 0), result.getEndTime());
    Assertions.assertNull(result.getRestaurant());
  }

  @Test
  @DisplayName("Deve retornar null quando model for null no toEntity com restaurant")
  void toEntityWithRestaurantsEntityShouldReturnNullWhenModelIsNull() {
    final var restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    final var result = mapper.toEntity(null, restaurantsEntity);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsEntity for null no toEntity")
  void toEntityWithRestaurantsEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final RestaurantOpeningHours model = Mockito.mock(RestaurantOpeningHours.class);

    final var result = mapper.toEntity(model, (RestaurantsEntity) null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve mapear domain para entity com restaurant informado")
  void toEntityWithRestaurantsEntityShouldMapDomainToEntity() {
    final var uuid = UUID.randomUUID();

    final var model = new RestaurantOpeningHours(
        uuid,
        DayOfWeek.THURSDAY,
        LocalTime.of(8, 30),
        LocalTime.of(19, 0)
    );

    final var restaurantsEntity = new RestaurantsEntity();

    final var result = mapper.toEntity(model, restaurantsEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(8, result.getStartTime()
        .getHour()
    );
    Assertions.assertEquals(19, result.getEndTime()
        .getHour()
    );
    Assertions.assertEquals(restaurantsEntity, result.getRestaurant());
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model")
  void toEntityWithExistingEntityShouldUpdateFields() {
    final var entity = new RestaurantOpeningHoursEntity();
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
  }
}
