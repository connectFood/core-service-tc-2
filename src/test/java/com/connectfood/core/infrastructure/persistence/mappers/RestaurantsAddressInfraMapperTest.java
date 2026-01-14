package com.connectfood.core.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsAddress;
import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsAddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsAddressInfraMapperTest {

  @Mock
  private RestaurantsInfraMapper restaurantsMapper;

  @Mock
  private AddressInfraMapper addressMapper;

  @InjectMocks
  private RestaurantsAddressInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final RestaurantsAddress result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain corretamente")
  void toDomainShouldMapEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final var restaurantsEntity = Mockito.mock(RestaurantsEntity.class);
    final var addressEntity = Mockito.mock(AddressEntity.class);

    final Restaurants restaurantsDomain = Mockito.mock(Restaurants.class);
    final Address addressDomain = Mockito.mock(Address.class);

    Mockito.when(restaurantsMapper.toDomain(restaurantsEntity)).thenReturn(restaurantsDomain);
    Mockito.when(addressMapper.toDomain(addressEntity)).thenReturn(addressDomain);

    final var entity = Mockito.mock(RestaurantsAddressEntity.class);
    Mockito.when(entity.getUuid()).thenReturn(uuid);
    Mockito.when(entity.getRestaurants()).thenReturn(restaurantsEntity);
    Mockito.when(entity.getAddress()).thenReturn(addressEntity);

    final RestaurantsAddress result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(restaurantsDomain, result.getRestaurants());
    Assertions.assertSame(addressDomain, result.getAddress());

    Mockito.verify(restaurantsMapper, Mockito.times(1)).toDomain(restaurantsEntity);
    Mockito.verify(addressMapper, Mockito.times(1)).toDomain(addressEntity);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsEntity for null no toEntity")
  void toEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);

    final RestaurantsAddressEntity result = mapper.toEntity(uuid, null, addressEntity);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando addressEntity for null no toEntity")
  void toEntityShouldReturnNullWhenAddressEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    final RestaurantsAddressEntity result = mapper.toEntity(uuid, restaurantsEntity, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve mapear para entity corretamente quando restaurantsEntity e addressEntity forem informados")
  void toEntityShouldMapToEntityCorrectly() {
    final var uuid = UUID.randomUUID();
    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);
    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);

    final RestaurantsAddressEntity result = mapper.toEntity(uuid, restaurantsEntity, addressEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(restaurantsEntity, result.getRestaurants());
    Assertions.assertSame(addressEntity, result.getAddress());

    Mockito.verifyNoInteractions(restaurantsMapper, addressMapper);
  }
}
