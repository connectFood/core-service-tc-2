package com.connectfood.core.application.restaurants.mapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantsAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RestaurantsAddressAppMapperTest {

  private final RestaurantsAddressAppMapper mapper = new RestaurantsAddressAppMapper();

  @Test
  @DisplayName("Deve criar o mapper pelo construtor padrão")
  void shouldCreateMapperUsingDefaultConstructor() {
    final var instance = new RestaurantsAddressAppMapper();
    Assertions.assertNotNull(instance);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurants for null")
  void shouldReturnNullWhenRestaurantsIsNull() {
    final Address address = Mockito.mock(Address.class);

    final RestaurantsAddress result = mapper.toDomain(null, address);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve retornar null quando address for null")
  void shouldReturnNullWhenAddressIsNull() {
    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    final RestaurantsAddress result = mapper.toDomain(restaurant, null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurants e address forem null")
  void shouldReturnNullWhenBothArgumentsAreNull() {
    final RestaurantsAddress result = mapper.toDomain(null, null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar RestaurantsAddress quando restaurants e address forem válidos")
  void shouldCreateRestaurantsAddressWhenValidData() {
    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final Address address = Mockito.mock(Address.class);

    final RestaurantsAddress result = mapper.toDomain(restaurant, address);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals(restaurant, result.getRestaurant());
    Assertions.assertEquals(address, result.getAddress());
  }
}
