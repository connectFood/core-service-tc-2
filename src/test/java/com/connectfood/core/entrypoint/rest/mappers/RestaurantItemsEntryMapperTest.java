package com.connectfood.core.entrypoint.rest.mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsImagesRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsImagesResponse;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsRequest;
import com.connectfood.core.entrypoint.rest.dto.restaurantitems.RestaurantItemsResponse;
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
class RestaurantItemsEntryMapperTest {

  @Mock
  private RestaurantsEntryMapper restaurantsMapper;

  @Mock
  private RestaurantItemsImagesEntryMapper restaurantItemsImagesMapper;

  @InjectMocks
  private RestaurantItemsEntryMapper mapper;

  @Test
  @DisplayName("toInput: deve retornar null quando request for null")
  void toInputShouldReturnNullWhenRequestIsNull() {
    final var result = mapper.toInput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toInput: deve mapear RestaurantItemsRequest para RestaurantItemsInput com lista vazia de imagens")
  void toInputShouldMapRequestToInputWithEmptyImages() {
    final var restaurantUuid = UUID.randomUUID();

    final var request = Mockito.mock(RestaurantItemsRequest.class);
    Mockito.when(request.getName()).thenReturn("ITEM");
    Mockito.when(request.getDescription()).thenReturn("DESC");
    Mockito.when(request.getValue()).thenReturn(BigDecimal.valueOf(10.50));
    Mockito.when(request.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(request.getRestaurantUuid()).thenReturn(restaurantUuid);
    Mockito.when(request.getImages()).thenReturn(List.of());

    final RestaurantItemsInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(10.50), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertEquals(restaurantUuid, result.getRestaurantUuid());
    Assertions.assertNotNull(result.getImages());
    Assertions.assertTrue(result.getImages().isEmpty());

    Mockito.verifyNoInteractions(restaurantsMapper);
    Mockito.verifyNoInteractions(restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve mapear imagens usando RestaurantItemsImagesEntryMapper")
  void shouldMapImages() {
    final var restaurantUuid = UUID.randomUUID();

    final RestaurantItemsImagesRequest imageRequest = Mockito.mock(RestaurantItemsImagesRequest.class);
    final RestaurantItemsImagesInput imageInput = Mockito.mock(RestaurantItemsImagesInput.class);
    Mockito.when(restaurantItemsImagesMapper.toInput(imageRequest)).thenReturn(imageInput);

    final var request = Mockito.mock(RestaurantItemsRequest.class);
    Mockito.when(request.getName()).thenReturn("ITEM");
    Mockito.when(request.getDescription()).thenReturn("DESC");
    Mockito.when(request.getValue()).thenReturn(BigDecimal.valueOf(10.50));
    Mockito.when(request.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(request.getRestaurantUuid()).thenReturn(restaurantUuid);
    Mockito.when(request.getImages()).thenReturn(List.of(imageRequest));

    final RestaurantItemsInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.getImages().size());
    Assertions.assertSame(imageInput, result.getImages().getFirst());

    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toInput(imageRequest);
    Mockito.verifyNoInteractions(restaurantsMapper);
    Mockito.verifyNoMoreInteractions(restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final var result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve mapear RestaurantItemsOutput para RestaurantItemsResponse com restaurante e imagens")
  void shouldMapOutputToResponseWithRestaurantAndImages() {
    final var restaurantOutput = Mockito.mock(RestaurantsOutput.class);
    final var restaurantResponse = Mockito.mock(RestaurantsResponse.class);
    Mockito.when(restaurantsMapper.toResponse(restaurantOutput)).thenReturn(restaurantResponse);

    final RestaurantItemsImagesOutput imageOutput = Mockito.mock(RestaurantItemsImagesOutput.class);
    final RestaurantItemsImagesResponse imageResponse = Mockito.mock(RestaurantItemsImagesResponse.class);
    Mockito.when(restaurantItemsImagesMapper.toResponse(imageOutput)).thenReturn(imageResponse);

    final var uuid = UUID.randomUUID();

    final var output = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(output.getUuid()).thenReturn(uuid);
    Mockito.when(output.getName()).thenReturn("ITEM");
    Mockito.when(output.getDescription()).thenReturn("DESC");
    Mockito.when(output.getValue()).thenReturn(BigDecimal.valueOf(15.00));
    Mockito.when(output.getRequestType()).thenReturn(RestaurantItemServiceType.LOCAL_ONLY);
    Mockito.when(output.getRestaurant()).thenReturn(restaurantOutput);
    Mockito.when(output.getImages()).thenReturn(List.of(imageOutput));

    final RestaurantItemsResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(15.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.LOCAL_ONLY, result.getRequestType());
    Assertions.assertSame(restaurantResponse, result.getRestaurant());
    Assertions.assertNotNull(result.getImages());
    Assertions.assertEquals(1, result.getImages().size());
    Assertions.assertSame(imageResponse, result.getImages().getFirst());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toResponse(restaurantOutput);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toResponse(imageOutput);
    Mockito.verifyNoMoreInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve mapear RestaurantItemsOutput para RestaurantItemsResponse sem restaurante e sem imagens")
  void shouldMapOutputToResponseWithoutRestaurantAndImages() {
    final var uuid = UUID.randomUUID();

    final var output = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(output.getUuid()).thenReturn(uuid);
    Mockito.when(output.getName()).thenReturn("ITEM");
    Mockito.when(output.getDescription()).thenReturn("DESC");
    Mockito.when(output.getValue()).thenReturn(BigDecimal.valueOf(20.00));
    Mockito.when(output.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(output.getRestaurant()).thenReturn(null);
    Mockito.when(output.getImages()).thenReturn(null);

    final RestaurantItemsResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(20.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertNull(result.getRestaurant());
    Assertions.assertNull(result.getImages());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }
}
