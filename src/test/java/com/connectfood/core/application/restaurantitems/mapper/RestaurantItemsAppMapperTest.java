package com.connectfood.core.application.restaurantitems.mapper;

import java.math.BigDecimal;
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
  @DisplayName("toDomain(input, restaurants): deve retornar null quando input for null")
  void toDomainShouldReturnNullWhenInputIsNull() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toDomain(input, restaurants): deve retornar null quando restaurants for null")
  void toDomainShouldReturnNullWhenRestaurantsIsNull() {
    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID(),
        List.of()
    );

    final var result = mapper.toDomain(input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toDomain(input, restaurants): deve mapear para domain com dados válidos")
  void toDomainShouldMapToDomainWithValidData() {
    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID(),
        List.of()
    );

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(input, restaurants);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(10.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertEquals(restaurants, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toDomain(uuid, input, restaurants): deve retornar null quando input for null")
  void toDomainWithUuidShouldReturnNullWhenInputIsNull() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(UUID.randomUUID(), null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toDomain(uuid, input, restaurants): deve retornar null quando restaurants for null")
  void toDomainWithUuidShouldReturnNullWhenRestaurantsIsNull() {
    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID(),
        List.of()
    );

    final var result = mapper.toDomain(UUID.randomUUID(), input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toDomain(uuid, input, restaurants): deve mapear para domain com UUID explícito e dados válidos")
  void toDomainWithUuidShouldMapToDomainWithExplicitUuid() {
    final var uuid = UUID.randomUUID();

    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID(),
        List.of()
    );

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(uuid, input, restaurants);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(10.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertEquals(restaurants, result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toOutput(model): deve retornar null quando model for null")
  void toOutputShouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toOutput(model): deve mapear para output quando restaurant estiver presente e images for null")
  void toOutputShouldMapToOutputWhenRestaurantIsPresentAndImagesIsNull() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var model = Mockito.mock(RestaurantItems.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("ITEM");
    Mockito.when(model.getDescription())
        .thenReturn("DESC");
    Mockito.when(model.getValue())
        .thenReturn(BigDecimal.valueOf(10.00));
    Mockito.when(model.getRequestType())
        .thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurants);
    Mockito.when(model.getImages())
        .thenReturn(null);

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants))
        .thenReturn(restaurantsOutput);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(10.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());
    Assertions.assertNull(result.getImages());

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toOutput(restaurants);
    Mockito.verifyNoInteractions(restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toOutput(model): deve mapear para output quando restaurant for null e images for null")
  void toOutputShouldMapToOutputWhenRestaurantIsNullAndImagesIsNull() {
    final var uuid = UUID.randomUUID();

    final var model = Mockito.mock(RestaurantItems.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("ITEM");
    Mockito.when(model.getDescription())
        .thenReturn("DESC");
    Mockito.when(model.getValue())
        .thenReturn(BigDecimal.valueOf(10.00));
    Mockito.when(model.getRequestType())
        .thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(model.getRestaurant())
        .thenReturn(null);
    Mockito.when(model.getImages())
        .thenReturn(null);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertNull(result.getRestaurant());
    Assertions.assertNull(result.getImages());

    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toOutput(model): deve mapear images quando model.getImages() não for null")
  void toOutputShouldMapImagesWhenModelImagesIsNotNull() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final RestaurantItemsImages img1 = Mockito.mock(RestaurantItemsImages.class);
    final RestaurantItemsImages img2 = Mockito.mock(RestaurantItemsImages.class);

    final var model = Mockito.mock(RestaurantItems.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("ITEM");
    Mockito.when(model.getDescription())
        .thenReturn("DESC");
    Mockito.when(model.getValue())
        .thenReturn(BigDecimal.valueOf(10.00));
    Mockito.when(model.getRequestType())
        .thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurants);
    Mockito.when(model.getImages())
        .thenReturn(List.of(img1, img2));

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants))
        .thenReturn(restaurantsOutput);

    final RestaurantItemsImagesOutput outImg1 = Mockito.mock(RestaurantItemsImagesOutput.class);
    final RestaurantItemsImagesOutput outImg2 = Mockito.mock(RestaurantItemsImagesOutput.class);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img1))
        .thenReturn(outImg1);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img2))
        .thenReturn(outImg2);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());
    Assertions.assertNotNull(result.getImages());
    Assertions.assertEquals(List.of(outImg1, outImg2), result.getImages());

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toOutput(restaurants);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1))
        .toOutput(img1);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1))
        .toOutput(img2);
  }

  @Test
  @DisplayName("toOutput(model, images): deve retornar null quando model for null")
  void toOutputWithImagesShouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null, List.of());

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, restaurantItemsImagesMapper);
  }

  @Test
  @DisplayName("toOutput(model, images): deve mapear images recebidas no parâmetro")
  void toOutputWithImagesShouldMapProvidedImages() {
    final var uuid = UUID.randomUUID();
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final RestaurantItemsImages img1 = Mockito.mock(RestaurantItemsImages.class);
    final RestaurantItemsImages img2 = Mockito.mock(RestaurantItemsImages.class);

    final var model = Mockito.mock(RestaurantItems.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("ITEM");
    Mockito.when(model.getDescription())
        .thenReturn("DESC");
    Mockito.when(model.getValue())
        .thenReturn(BigDecimal.valueOf(10.00));
    Mockito.when(model.getRequestType())
        .thenReturn(RestaurantItemServiceType.DELIVERY);
    Mockito.when(model.getRestaurant())
        .thenReturn(restaurants);

    final RestaurantsOutput restaurantsOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(restaurantsMapper.toOutput(restaurants))
        .thenReturn(restaurantsOutput);

    final RestaurantItemsImagesOutput outImg1 = Mockito.mock(RestaurantItemsImagesOutput.class);
    final RestaurantItemsImagesOutput outImg2 = Mockito.mock(RestaurantItemsImagesOutput.class);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img1))
        .thenReturn(outImg1);
    Mockito.when(restaurantItemsImagesMapper.toOutput(img2))
        .thenReturn(outImg2);

    final var result = mapper.toOutput(model, List.of(img1, img2));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(restaurantsOutput, result.getRestaurant());
    Assertions.assertEquals(List.of(outImg1, outImg2), result.getImages());

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toOutput(restaurants);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1))
        .toOutput(img1);
    Mockito.verify(restaurantItemsImagesMapper, Mockito.times(1))
        .toOutput(img2);
  }

  @Test
  @DisplayName(
      "toDomain: deve propagar BadRequestException quando input tiver dados inválidos (validação é do domínio)")
  void toDomainShouldPropagateBadRequestExceptionWhenInputIsInvalid() {
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
}
