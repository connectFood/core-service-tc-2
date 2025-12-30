package com.connectfood.core.entrypoint.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurantstype.RestaurantsTypeResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsEntryMapperTest {

  @Mock
  private RestaurantsTypeEntryMapper restaurantsTypeMapper;

  @InjectMocks
  private RestaurantsEntryMapper mapper;

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final RestaurantsInput result = mapper.toInput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido")
  void shouldConvertToInputWhenRequestIsProvided() {
    final var restaurantsTypeUuid = UUID.randomUUID();
    final RestaurantsRequest request = new RestaurantsRequest("Restaurant A", restaurantsTypeUuid);

    final RestaurantsInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Restaurant A", result.getName());
    Assertions.assertEquals(restaurantsTypeUuid, result.getRestaurantsTypeUuid());

    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final RestaurantsResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando restaurantsType estiver presente")
  void shouldConvertToResponseWhenRestaurantsTypeIsPresent() {
    final var uuid = UUID.randomUUID();

    final RestaurantsTypeOutput typeOutput = Mockito.mock(RestaurantsTypeOutput.class);
    final RestaurantsTypeResponse typeResponse = Mockito.mock(RestaurantsTypeResponse.class);

    Mockito.when(restaurantsTypeMapper.toResponse(typeOutput))
        .thenReturn(typeResponse);

    final RestaurantsOutput output = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getName())
        .thenReturn("Restaurant B");
    Mockito.when(output.getRestaurantsType())
        .thenReturn(typeOutput);

    final RestaurantsResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant B", result.getName());
    Assertions.assertSame(typeResponse, result.getRestaurantsType());

    Mockito.verify(restaurantsTypeMapper, Mockito.times(1))
        .toResponse(typeOutput);
    Mockito.verifyNoMoreInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando restaurantsType for null")
  void shouldConvertToResponseWhenRestaurantsTypeIsNull() {
    final var uuid = UUID.randomUUID();

    final RestaurantsOutput output = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getName())
        .thenReturn("Restaurant C");
    Mockito.when(output.getRestaurantsType())
        .thenReturn(null);

    final RestaurantsResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant C", result.getName());
    Assertions.assertNull(result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }
}
