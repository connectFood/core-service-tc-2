package com.connectfood.core.entrypoint.rest.mappers;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsEntryMapperTest {

  @Mock
  private RestaurantsEntryMapper restaurantsMapper;

  @InjectMocks
  private RestaurantItemsEntryMapper mapper;

  @Test
  @DisplayName("toInput: deve retornar null quando request for null")
  void toInputShouldReturnNullWhenRequestIsNull() {
    final var result = mapper.toInput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toInput: deve mapear RestaurantItemsRequest para RestaurantItemsInput")
  void toInputShouldMapRequestToInput() {
    final var request = Mockito.mock(RestaurantItemsRequest.class);

    Mockito.when(request.getName()).thenReturn("ITEM");
    Mockito.when(request.getDescription()).thenReturn("DESC");
    Mockito.when(request.getValue()).thenReturn(BigDecimal.valueOf(10.50));
    Mockito.when(request.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(request.getRestaurantUuid()).thenReturn(UUID.randomUUID());

    final RestaurantItemsInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(10.50), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertEquals(request.getRestaurantUuid(), result.getRestaurantUuid());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toResponse: deve retornar null quando output for null")
  void toResponseShouldReturnNullWhenOutputIsNull() {
    final var result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toResponse: deve mapear RestaurantItemsOutput para RestaurantItemsResponse com restaurante")
  void toResponseShouldMapOutputToResponseWithRestaurant() {
    final var restaurantOutput = Mockito.mock(RestaurantsOutput.class);
    final var restaurantResponse = Mockito.mock(
        com.connectfood.core.entrypoint.rest.dto.restaurants.RestaurantsResponse.class
    );

    Mockito.when(restaurantsMapper.toResponse(restaurantOutput)).thenReturn(restaurantResponse);

    final var output = Mockito.mock(RestaurantItemsOutput.class);

    Mockito.when(output.getUuid()).thenReturn(UUID.randomUUID());
    Mockito.when(output.getName()).thenReturn("ITEM");
    Mockito.when(output.getDescription()).thenReturn("DESC");
    Mockito.when(output.getValue()).thenReturn(BigDecimal.valueOf(15.00));
    Mockito.when(output.getRequestType()).thenReturn(RestaurantItemServiceType.LOCAL_ONLY);
    Mockito.when(output.getRestaurant()).thenReturn(restaurantOutput);

    final RestaurantItemsResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(15.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.LOCAL_ONLY, result.getRequestType());
    Assertions.assertEquals(restaurantResponse, result.getRestaurant());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toResponse(restaurantOutput);
  }

  @Test
  @DisplayName("toResponse: deve mapear RestaurantItemsOutput para RestaurantItemsResponse sem restaurante")
  void toResponseShouldMapOutputToResponseWithoutRestaurant() {
    final var output = Mockito.mock(RestaurantItemsOutput.class);

    Mockito.when(output.getUuid()).thenReturn(UUID.randomUUID());
    Mockito.when(output.getName()).thenReturn("ITEM");
    Mockito.when(output.getDescription()).thenReturn("DESC");
    Mockito.when(output.getValue()).thenReturn(BigDecimal.valueOf(20.00));
    Mockito.when(output.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(output.getRestaurant()).thenReturn(null);

    final RestaurantItemsResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(20.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertNull(result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
  }
}
