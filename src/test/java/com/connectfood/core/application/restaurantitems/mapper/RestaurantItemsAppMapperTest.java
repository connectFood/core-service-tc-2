package com.connectfood.core.application.restaurantitems.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsImagesOutput;
import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.model.RestaurantItemsImages;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantItemsAppMapperTest {

  @Mock
  private RestaurantsAppMapper restaurantsMapper;

  @Mock
  private RestaurantItemsImagesAppMapper restaurantItemsImagesMapper;

  @InjectMocks
  private RestaurantItemsAppMapper mapper;

  @Test
  @DisplayName("Não deve criar domínio quando input for null")
  void shouldReturnNullWhenInputIsNull() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Não deve criar domínio quando restaurants for null")
  void shouldReturnNullWhenRestaurantsIsNull() {
    final var input = buildValidInput();

    final var result = mapper.toDomain(input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve criar domínio quando input e restaurants forem válidos")
  void shouldCreateDomainWhenValidData() {
    final var input = buildValidInput();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(input, restaurants);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals(input.getName(), result.getName());
    Assertions.assertEquals(input.getDescription(), result.getDescription());
    Assertions.assertEquals(input.getValue(), result.getValue());
    Assertions.assertEquals(input.getRequestType(), result.getRequestType());
    Assertions.assertEquals(restaurants, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Não deve criar domínio com UUID quando input for null")
  void shouldReturnNullWhenInputIsNullEvenWithUuid() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(UUID.randomUUID(), null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Não deve criar domínio com UUID quando restaurants for null")
  void shouldReturnNullWhenRestaurantsIsNullEvenWithUuid() {
    final var input = buildValidInput();

    final var result = mapper.toDomain(UUID.randomUUID(), input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve criar domínio com UUID explícito quando dados forem válidos")
  void shouldCreateDomainWithExplicitUuidWhenValidData() {
    final var uuid = UUID.randomUUID();
    final var input = buildValidInput();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(uuid, input, restaurants);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(input.getName(), result.getName());
    Assertions.assertEquals(input.getDescription(), result.getDescription());
    Assertions.assertEquals(input.getValue(), result.getValue());
    Assertions.assertEquals(input.getRequestType(), result.getRequestType());
    Assertions.assertEquals(restaurants, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Não deve criar output quando model for null")
  void shouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve criar output quando restaurant existir e images for null")
  void shouldCreateOutputWhenRestaurantExistsAndImagesIsNull() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var model = mockModelWithoutImages(uuid, restaurants);

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants)).thenReturn(restaurantsOutput);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(10.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());
    Assertions.assertEquals(List.of(), result.getImages());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toOutput(restaurants);
    Mockito.verifyNoInteractions(restaurantItemsImagesMapper);
    Mockito.verifyNoMoreInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve criar output quando restaurant e images forem null")
  void shouldCreateOutputWhenRestaurantAndImagesAreNull() {
    final var uuid = UUID.randomUUID();

    final var model = mockModelWithoutImages(uuid, null);
    Mockito.when(model.getImages()).thenReturn(null);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertNull(result.getRestaurant());
    Assertions.assertNull(result.getImages());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve criar output com images quando model possuir images")
  void shouldCreateOutputWithImagesWhenModelHasImages() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final RestaurantItemsImages img1 = Mockito.mock(RestaurantItemsImages.class);
    final RestaurantItemsImages img2 = Mockito.mock(RestaurantItemsImages.class);

    final var model = mockModelWithImages(uuid, restaurants, List.of(img1, img2));

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants)).thenReturn(restaurantsOutput);

    final RestaurantItemsImagesOutput outImg1 = Mockito.mock(RestaurantItemsImagesOutput.class);
    final RestaurantItemsImagesOutput outImg2 = Mockito.mock(RestaurantItemsImagesOutput.class);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img1)).thenReturn(outImg1);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img2)).thenReturn(outImg2);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());
    Assertions.assertNotNull(result.getImages());
    Assertions.assertEquals(List.of(outImg1, outImg2), result.getImages());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toOutput(restaurants);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toOutput(img1);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toOutput(img2);
    Mockito.verifyNoMoreInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Não deve criar output no método com images quando model for null")
  void shouldReturnNullWhenModelIsNullOnOutputWithImages() {
    final var result = mapper.toOutput(null, List.of());

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve criar output usando images recebidas por parâmetro")
  void shouldCreateOutputUsingImagesProvidedAsParameter() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final RestaurantItemsImages img1 = Mockito.mock(RestaurantItemsImages.class);
    final RestaurantItemsImages img2 = Mockito.mock(RestaurantItemsImages.class);

    final var model = mockModelWithoutImages(uuid, restaurants);

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants)).thenReturn(restaurantsOutput);

    final RestaurantItemsImagesOutput outImg1 = Mockito.mock(RestaurantItemsImagesOutput.class);
    final RestaurantItemsImagesOutput outImg2 = Mockito.mock(RestaurantItemsImagesOutput.class);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img1)).thenReturn(outImg1);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img2)).thenReturn(outImg2);

    final var result = mapper.toOutput(model, List.of(img1, img2));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());
    Assertions.assertEquals(List.of(outImg1, outImg2), result.getImages());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toOutput(restaurants);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toOutput(img1);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toOutput(img2);
    Mockito.verifyNoMoreInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve criar output com restaurant null quando model.getRestaurant() for null no método com images")
  void shouldCreateOutputWithNullRestaurantWhenRestaurantIsNullOnOutputWithImages() {
    final var uuid = UUID.randomUUID();
    final var model = mockModelWithoutImages(uuid, null);

    final RestaurantItemsImages img1 = Mockito.mock(RestaurantItemsImages.class);

    final RestaurantItemsImagesOutput outImg1 = Mockito.mock(RestaurantItemsImagesOutput.class);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img1)).thenReturn(outImg1);

    final var result = mapper.toOutput(model, List.of(img1));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertNull(result.getRestaurant());
    Assertions.assertEquals(List.of(outImg1), result.getImages());

    Mockito.verifyNoInteractions(restaurantsMapper);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1)).toOutput(img1);
    Mockito.verifyNoMoreInteractions(restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("Deve criar output com lista vazia quando images estiver vazio no método com images")
  void shouldCreateOutputWithEmptyImagesListWhenImagesIsEmptyOnOutputWithImages() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var model = mockModelWithoutImages(uuid, restaurants);

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants)).thenReturn(restaurantsOutput);

    final var result = mapper.toOutput(model, Collections.emptyList());

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());
    Assertions.assertNotNull(result.getImages());
    Assertions.assertTrue(result.getImages().isEmpty());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toOutput(restaurants);
    Mockito.verifyNoInteractions(restaurantItemsImagesMapper);
    Mockito.verifyNoMoreInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("Deve lançar BadRequestException quando input possuir dados inválidos")
  void shouldThrowBadRequestExceptionWhenInputIsInvalid() {
    final var input = new RestaurantItemsInput(
        "",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID(),
        List.of()
    );

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> mapper.toDomain(input, restaurants)
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  private static RestaurantItemsInput buildValidInput() {
    return new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID(),
        List.of()
    );
  }

  private static RestaurantItems mockModelWithoutImages(final UUID uuid, final Restaurants restaurants) {
    final var model = Mockito.mock(RestaurantItems.class);
    Mockito.when(model.getUuid()).thenReturn(uuid);
    Mockito.when(model.getName()).thenReturn("ITEM");
    Mockito.when(model.getDescription()).thenReturn("DESC");
    Mockito.when(model.getValue()).thenReturn(BigDecimal.valueOf(10.00));
    Mockito.when(model.getRequestType()).thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(model.getRestaurant()).thenReturn(restaurants);
    return model;
  }

  private static RestaurantItems mockModelWithImages(
      final UUID uuid,
      final Restaurants restaurants,
      final List<RestaurantItemsImages> images
  ) {
    final var model = mockModelWithoutImages(uuid, restaurants);
    Mockito.when(model.getImages()).thenReturn(images);
    return model;
  }
}
