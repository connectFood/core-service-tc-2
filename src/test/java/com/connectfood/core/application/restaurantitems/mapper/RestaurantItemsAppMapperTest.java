package com.connectfood.core.application.restaurantitems.mapper;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.RestaurantItems;
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

  @InjectMocks
  private RestaurantItemsAppMapper mapper;

  @Test
  @DisplayName("toDomain(input, restaurants): deve retornar null quando input for null")
  void toDomainShouldReturnNullWhenInputIsNull() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toDomain(input, restaurants): deve retornar null quando restaurants for null")
  void toDomainShouldReturnNullWhenRestaurantsIsNull() {
    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID()
    );

    final var result = mapper.toDomain(input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toDomain(input, restaurants): deve mapear para domain com dados válidos")
  void toDomainShouldMapToDomainWithValidData() {
    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID()
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

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toDomain(uuid, input, restaurants): deve retornar null quando input for null")
  void toDomainWithUuidShouldReturnNullWhenInputIsNull() {
    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var result = mapper.toDomain(UUID.randomUUID(), null, restaurants);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toDomain(uuid, input, restaurants): deve retornar null quando restaurants for null")
  void toDomainWithUuidShouldReturnNullWhenRestaurantsIsNull() {
    final var input = new RestaurantItemsInput(
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        UUID.randomUUID()
    );

    final var result = mapper.toDomain(UUID.randomUUID(), input, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
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
        UUID.randomUUID()
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

    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toOutput: deve retornar null quando model for null")
  void toOutputShouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper);
  }

  @Test
  @DisplayName("toOutput: deve mapear para output quando restaurant estiver presente")
  void toOutputShouldMapToOutputWhenRestaurantIsPresent() {
    final var uuid = UUID.randomUUID();

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var model = new RestaurantItems(
        uuid,
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        restaurants
    );

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

    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toOutput(restaurants);
  }

  @Test
  @DisplayName("toOutput: deve mapear para output quando restaurant for null (sem chamar restaurantsMapper)")
  void toOutputShouldMapToOutputWhenRestaurantIsNull() {
    final var uuid = UUID.randomUUID();

    final var model = new RestaurantItems(
        uuid,
        "ITEM",
        "DESC",
        BigDecimal.valueOf(10.00),
        RestaurantItemServiceType.DELIVERY,
        null
    );

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ITEM", result.getName());
    Assertions.assertEquals("DESC", result.getDescription());
    Assertions.assertEquals(BigDecimal.valueOf(10.00), result.getValue());
    Assertions.assertEquals(RestaurantItemServiceType.DELIVERY, result.getRequestType());
    Assertions.assertNull(result.getRestaurant());

    Mockito.verifyNoInteractions(restaurantsMapper);
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
        UUID.randomUUID()
    );

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> mapper.toDomain(input, restaurants)
    );

    Assertions.assertEquals("Name is required", exception.getMessage());
    Mockito.verifyNoInteractions(restaurantsMapper);
  }
}
