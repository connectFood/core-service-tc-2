package com.connectfood.core.application.restaurantopeninghours.mapper;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantOpeningHoursAppMapper;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantOpeningHoursAppMapperTest {

  @Mock
  private RestaurantsAppMapper restaurantsMapper;

  @InjectMocks
  private RestaurantOpeningHoursAppMapper mapper;

  @Test
  @DisplayName("Não deve criar domínio quando input for null")
  void shouldReturnNullWhenInputIsNull() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Não deve criar domínio quando restaurants for null")
  void shouldReturnNullWhenRestaurantsIsNull() {
    final RestaurantOpeningHoursInput input = Mockito.mock(RestaurantOpeningHoursInput.class);

    final var result = mapper.toDomain(input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve criar domínio quando input e restaurants forem válidos")
  void shouldCreateDomainWhenValidData() {
    final var input = Mockito.mock(RestaurantOpeningHoursInput.class);
    Mockito.when(input.getDayOfWeek())
        .thenReturn(DayOfWeek.MONDAY);
    Mockito.when(input.getStartTime())
        .thenReturn(LocalTime.of(9, 0));
    Mockito.when(input.getEndTime())
        .thenReturn(LocalTime.of(18, 0));

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(input, restaurants);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid(), "UUID deve ser gerado pelo domínio quando não informado");
    Assertions.assertEquals(DayOfWeek.MONDAY, result.getDayWeek());
    Assertions.assertEquals(LocalTime.of(9, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(18, 0), result.getEndTime());
    Assertions.assertEquals(restaurants, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Não deve criar domínio com UUID quando input for null")
  void shouldReturnNullWhenInputIsNullEvenWithUuid() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(UUID.randomUUID(), null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Não deve criar domínio com UUID quando restaurants for null")
  void shouldReturnNullWhenRestaurantsIsNullEvenWithUuid() {
    final RestaurantOpeningHoursInput input = Mockito.mock(RestaurantOpeningHoursInput.class);

    final var result = mapper.toDomain(UUID.randomUUID(), input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve criar domínio com UUID explícito quando dados forem válidos")
  void shouldCreateDomainWithExplicitUuidWhenValidData() {
    final var uuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantOpeningHoursInput.class);
    Mockito.when(input.getDayOfWeek())
        .thenReturn(DayOfWeek.FRIDAY);
    Mockito.when(input.getStartTime())
        .thenReturn(LocalTime.of(10, 0));
    Mockito.when(input.getEndTime())
        .thenReturn(LocalTime.of(22, 0));

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(uuid, input, restaurants);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.FRIDAY, result.getDayWeek());
    Assertions.assertEquals(LocalTime.of(10, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(22, 0), result.getEndTime());
    Assertions.assertEquals(restaurants, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Não deve criar output quando model for null")
  void shouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve criar output com restaurant quando model.getRestaurant() estiver presente")
  void shouldCreateOutputWhenRestaurantIsPresent() {
    final var uuid = UUID.randomUUID();

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var model = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getDayWeek())
        .thenReturn(DayOfWeek.MONDAY);
    Mockito.when(model.getStartTime())
        .thenReturn(LocalTime.of(9, 0));
    Mockito.when(model.getEndTime())
        .thenReturn(LocalTime.of(18, 0));
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurants);

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants))
        .thenReturn(restaurantsOutput);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(9, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(18, 0), result.getEndTime());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toOutput(restaurants);
    Mockito.verifyNoMoreInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve criar output com restaurant null quando model.getRestaurant() for null")
  void shouldCreateOutputWithNullRestaurantWhenRestaurantIsNull() {
    final var uuid = UUID.randomUUID();

    final var model = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getDayWeek())
        .thenReturn(DayOfWeek.SUNDAY);
    Mockito.when(model.getStartTime())
        .thenReturn(LocalTime.of(8, 0));
    Mockito.when(model.getEndTime())
        .thenReturn(LocalTime.of(12, 0));
    Mockito.when(model.getRestaurant())
        .thenReturn(null);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.SUNDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(8, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(12, 0), result.getEndTime());
    Assertions.assertNull(result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }
}
