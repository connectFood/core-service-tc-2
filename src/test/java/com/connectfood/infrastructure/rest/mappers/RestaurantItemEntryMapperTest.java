package com.connectfood.infrastructure.rest.mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitem.dto.RestaurantItemImageInput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemImageOutput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemInput;
import com.connectfood.core.application.restaurantitem.dto.RestaurantItemOutput;
import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemImageRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemImageResponse;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemRequest;
import com.connectfood.infrastructure.rest.dto.restaurantitem.RestaurantItemResponse;
import com.connectfood.infrastructure.rest.dto.restaurant.RestaurantResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemEntryMapperTest {

  @Mock
  private RestaurantEntryMapper restaurantsMapper;

  @Mock
  private RestaurantItemImageEntryMapper restaurantItemsImagesMapper;

  @InjectMocks
  private RestaurantItemEntryMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando o request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final var result = mapper.toInput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve mapear o request para input com lista vazia de imagens")
  void shouldMapRequestToInputWithEmptyImages() {
    final var restaurantUuid = UUID.randomUUID();

    final var request = Mockito.mock(RestaurantItemRequest.class);
    Mockito.when(request.getName()).thenReturn("ITEM");
    Mockito.when(request.getDescription()).thenReturn("DESC");
    Mockito.when(request.getValue()).thenReturn(BigDecimal.valueOf(10.50));
    Mockito.when(request.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(request.getRestaurantUuid()).thenReturn(restaurantUuid);
    Mockito.when(request.getImages()).thenReturn(List.of());

    final RestaurantItemInput result = mapper.toInput(request);

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
  @DisplayName("Deve mapear as imagens utilizando o mapper de imagens")
  void shouldMapImagesUsingImagesMapper() {
    final var restaurantUuid = UUID.randomUUID();

    final RestaurantItemImageRequest imageRequest = Mockito.mock(RestaurantItemImageRequest.class);
    final RestaurantItemImageInput imageInput = Mockito.mock(RestaurantItemImageInput.class);
    Mockito.when(restaurantItemsImagesMapper.toInput(imageRequest)).thenReturn(imageInput);

    final var request = Mockito.mock(RestaurantItemRequest.class);
    Mockito.when(request.getName()).thenReturn("ITEM");
    Mockito.when(request.getDescription()).thenReturn("DESC");
    Mockito.when(request.getValue()).thenReturn(BigDecimal.valueOf(10.50));
    Mockito.when(request.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(request.getRestaurantUuid()).thenReturn(restaurantUuid);
    Mockito.when(request.getImages()).thenReturn(List.of(imageRequest));

    final RestaurantItemInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.getImages().size());
    Assertions.assertSame(imageInput, result.getImages().getFirst());

    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toInput(imageRequest);
    Mockito.verifyNoInteractions(restaurantsMapper);
    Mockito.verifyNoMoreInteractions(restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando o output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final var result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve mapear o output para response com restaurante e imagens")
  void shouldMapOutputToResponseWithRestaurantAndImages() {
    final var restaurantOutput = Mockito.mock(RestaurantOutput.class);
    final var restaurantResponse = Mockito.mock(RestaurantResponse.class);
    Mockito.when(restaurantsMapper.toResponse(restaurantOutput)).thenReturn(restaurantResponse);

    final RestaurantItemImageOutput imageOutput = Mockito.mock(RestaurantItemImageOutput.class);
    final RestaurantItemImageResponse imageResponse = Mockito.mock(RestaurantItemImageResponse.class);
    Mockito.when(restaurantItemsImagesMapper.toResponse(imageOutput)).thenReturn(imageResponse);

    final var uuid = UUID.randomUUID();

    final var output = Mockito.mock(RestaurantItemOutput.class);
    Mockito.when(output.getUuid()).thenReturn(uuid);
    Mockito.when(output.getName()).thenReturn("ITEM");
    Mockito.when(output.getDescription()).thenReturn("DESC");
    Mockito.when(output.getValue()).thenReturn(BigDecimal.valueOf(15.00));
    Mockito.when(output.getRequestType()).thenReturn(RestaurantItemServiceType.LOCAL_ONLY);
    Mockito.when(output.getRestaurant()).thenReturn(restaurantOutput);
    Mockito.when(output.getImages()).thenReturn(List.of(imageOutput));

    final RestaurantItemResponse result = mapper.toResponse(output);

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
  @DisplayName("Deve mapear o output para response sem restaurante e sem imagens")
  void shouldMapOutputToResponseWithoutRestaurantAndImages() {
    final var uuid = UUID.randomUUID();

    final var output = Mockito.mock(RestaurantItemOutput.class);
    Mockito.when(output.getUuid()).thenReturn(uuid);
    Mockito.when(output.getName()).thenReturn("ITEM");
    Mockito.when(output.getDescription()).thenReturn("DESC");
    Mockito.when(output.getValue()).thenReturn(BigDecimal.valueOf(20.00));
    Mockito.when(output.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(output.getRestaurant()).thenReturn(null);
    Mockito.when(output.getImages()).thenReturn(null);

    final RestaurantItemResponse result = mapper.toResponse(output);

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
