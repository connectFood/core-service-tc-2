package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantAddress;
import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantAddressEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantAddressInfraMapperTest {

  @Mock
  private RestaurantInfraMapper restaurantsMapper;

  @Mock
  private AddressInfraMapper addressMapper;

  @InjectMocks
  private RestaurantAddressInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final RestaurantAddress result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain corretamente")
  void toDomainShouldMapEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var restaurantsEntity = Mockito.mock(RestaurantEntity.class);
    final var addressEntity = Mockito.mock(AddressEntity.class);

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    final Address addressDomain = Mockito.mock(Address.class);

    Mockito.when(restaurantsMapper.toDomain(restaurantsEntity)).thenReturn(restaurantDomain);
    Mockito.when(addressMapper.toDomain(addressEntity)).thenReturn(addressDomain);

    final var entity = Mockito.mock(RestaurantAddressEntity.class);
    Mockito.when(entity.getUuid()).thenReturn(uuid);
    Mockito.when(entity.getRestaurants()).thenReturn(restaurantsEntity);
    Mockito.when(entity.getAddress()).thenReturn(addressEntity);

    final RestaurantAddress result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(restaurantDomain, result.getRestaurant());
    Assertions.assertSame(addressDomain, result.getAddress());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toDomain(restaurantsEntity);
    Mockito.verify(addressMapper, Mockito.times(1)).toDomain(addressEntity);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsEntity for null no toEntity")
  void toEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);

    final RestaurantAddressEntity result = mapper.toEntity(uuid, null, addressEntity);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando addressEntity for null no toEntity")
  void toEntityShouldReturnNullWhenAddressEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);

    final RestaurantAddressEntity result = mapper.toEntity(uuid, restaurantEntity, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve mapear para entity corretamente quando restaurantsEntity e addressEntity forem informados")
  void toEntityShouldMapToEntityCorrectly() {
    final var uuid = UUID.randomUUID();
    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);
    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);

    final RestaurantAddressEntity result = mapper.toEntity(uuid, restaurantEntity, addressEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(restaurantEntity, result.getRestaurants());
    Assertions.assertSame(addressEntity, result.getAddress());

    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }
}
