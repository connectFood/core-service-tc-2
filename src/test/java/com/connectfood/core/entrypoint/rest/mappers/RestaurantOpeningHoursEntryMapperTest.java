package com.connectfood.core.entrypoint.rest.mappers;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursInput;
import com.connectfood.core.application.restaurantopeninghours.dto.RestaurantOpeningHoursOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantopeninghours.RestaurantOpeningHoursResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantOpeningHoursEntryMapperTest {

  @Mock
  private RestaurantsEntryMapper restaurantsMapper;

  @InjectMocks
  private RestaurantOpeningHoursEntryMapper mapper;

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantOpeningHoursInput result = mapper.toInput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido")
  void shouldConvertToInputWhenRequestIsProvided() {
    final var restaurantUuid = UUID.randomUUID();
    final var request = new RestaurantOpeningHoursRequest(
        DayOfWeek.MONDAY,
        LocalTime.of(9, 0),
        LocalTime.of(18, 0),
        restaurantUuid
    );

    final RestaurantOpeningHoursInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(DayOfWeek.MONDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(9, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(18, 0), result.getEndTime());
    Assertions.assertEquals(restaurantUuid, result.getRestaurantUuid());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantOpeningHoursResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando restaurant estiver presente")
  void shouldConvertToResponseWhenRestaurantIsPresent() {
    final var uuid = UUID.randomUUID();

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    final RestaurantsResponse restaurantsResponse = Mockito.mock(RestaurantsResponse.class);

    Mockito.when(restaurantsMapper.toResponse(restaurantsOutput))
        .thenReturn(restaurantsResponse);

    final RestaurantOpeningHoursOutput output = Mockito.mock(RestaurantOpeningHoursOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getDayOfWeek())
        .thenReturn(DayOfWeek.FRIDAY);
    Mockito.when(output.getStartTime())
        .thenReturn(LocalTime.of(10, 0));
    Mockito.when(output.getEndTime())
        .thenReturn(LocalTime.of(22, 0));
    Mockito.when(output.getRestaurant())
        .thenReturn(restaurantsOutput);

    final RestaurantOpeningHoursResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.FRIDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(10, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(22, 0), result.getEndTime());
    Assertions.assertSame(restaurantsResponse, result.getRestaurant());

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toResponse(restaurantsOutput);
    Mockito.verifyNoMoreInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando restaurant for null")
  void shouldConvertToResponseWhenRestaurantIsNull() {
    final var uuid = UUID.randomUUID();

    final RestaurantOpeningHoursOutput output = Mockito.mock(RestaurantOpeningHoursOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getDayOfWeek())
        .thenReturn(DayOfWeek.SUNDAY);
    Mockito.when(output.getStartTime())
        .thenReturn(LocalTime.of(8, 0));
    Mockito.when(output.getEndTime())
        .thenReturn(LocalTime.of(12, 0));
    Mockito.when(output.getRestaurant())
        .thenReturn(null);

    final RestaurantOpeningHoursResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(DayOfWeek.SUNDAY, result.getDayOfWeek());
    Assertions.assertEquals(LocalTime.of(8, 0), result.getStartTime());
    Assertions.assertEquals(LocalTime.of(12, 0), result.getEndTime());
    Assertions.assertNull(result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }
}
