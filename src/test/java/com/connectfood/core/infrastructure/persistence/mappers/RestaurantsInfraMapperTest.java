package com.connectfood.core.infrastructure.persistence.mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantOpeningHoursEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsTypeEntity;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsInfraMapperTest {

  @Mock
  private RestaurantsTypeInfraMapper restaurantsTypeMapper;

  @Mock
  private RestaurantOpeningHoursInfraMapper restaurantOpeningHoursMapper;

  @Mock
  private AddressInfraMapper addressMapper;

  @Mock
  private UsersInfraMapper usersMapper;

  @InjectMocks
  private RestaurantsInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final Restaurants result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain com restaurantsType, openingHours, address e users")
  void toDomainShouldMapEntityToDomainWhenAllDataIsPresent() {
    final var uuid = UUID.randomUUID();
    final var name = "RESTAURANT";

    final RestaurantsTypeEntity typeEntity = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantsType typeDomain = Mockito.mock(RestaurantsType.class);

    final RestaurantOpeningHoursEntity ohEntity1 = Mockito.mock(RestaurantOpeningHoursEntity.class);
    final RestaurantOpeningHoursEntity ohEntity2 = Mockito.mock(RestaurantOpeningHoursEntity.class);

    final RestaurantOpeningHours ohDomain1 = Mockito.mock(RestaurantOpeningHours.class);
    final RestaurantOpeningHours ohDomain2 = Mockito.mock(RestaurantOpeningHours.class);

    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);
    final Address addressDomain = Mockito.mock(Address.class);

    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);
    final Users usersDomain = Mockito.mock(Users.class);

    Mockito.when(restaurantsTypeMapper.toDomain(typeEntity))
        .thenReturn(typeDomain);
    Mockito.when(restaurantOpeningHoursMapper.toDomain(ohEntity1))
        .thenReturn(ohDomain1);
    Mockito.when(restaurantOpeningHoursMapper.toDomain(ohEntity2))
        .thenReturn(ohDomain2);
    Mockito.when(addressMapper.toDomain(addressEntity))
        .thenReturn(addressDomain);
    Mockito.when(usersMapper.toDomain(usersEntity))
        .thenReturn(usersDomain);

    final RestaurantsEntity entity = Mockito.mock(RestaurantsEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getName())
        .thenReturn(name);
    Mockito.when(entity.getRestaurantsType())
        .thenReturn(typeEntity);
    Mockito.when(entity.getOpeningHours())
        .thenReturn(Set.of(ohEntity1, ohEntity2));
    Mockito.when(entity.getAddress())
        .thenReturn(addressEntity);
    Mockito.when(entity.getUsers())
        .thenReturn(usersEntity);

    final Restaurants result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertSame(typeDomain, result.getRestaurantsType());
    Assertions.assertEquals(List.of(ohDomain1, ohDomain2), result.getOpeningHours());
    Assertions.assertSame(addressDomain, result.getAddress());
    Assertions.assertSame(usersDomain, result.getUsers());

    Mockito.verify(restaurantsTypeMapper)
        .toDomain(typeEntity);
    Mockito.verify(restaurantOpeningHoursMapper)
        .toDomain(ohEntity1);
    Mockito.verify(restaurantOpeningHoursMapper)
        .toDomain(ohEntity2);
    Mockito.verify(addressMapper)
        .toDomain(addressEntity);
    Mockito.verify(usersMapper)
        .toDomain(usersEntity);
  }

  @Test
  @DisplayName("Deve mapear entity para domain com opcionais nulos e openingHours vazio")
  void toDomainShouldMapEntityToDomainWhenOptionalRelationsAreNull() {
    final var uuid = UUID.randomUUID();
    final var name = "RESTAURANT";

    final RestaurantsTypeEntity typeEntity = Mockito.mock(RestaurantsTypeEntity.class);
    final RestaurantsType typeDomain = Mockito.mock(RestaurantsType.class);

    Mockito.when(restaurantsTypeMapper.toDomain(typeEntity))
        .thenReturn(typeDomain);

    final RestaurantsEntity entity = Mockito.mock(RestaurantsEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getName())
        .thenReturn(name);
    Mockito.when(entity.getRestaurantsType())
        .thenReturn(typeEntity);

    // crucial: evita NPE no stream()
    Mockito.when(entity.getOpeningHours())
        .thenReturn(Set.of());

    Mockito.when(entity.getAddress())
        .thenReturn(null);
    Mockito.when(entity.getUsers())
        .thenReturn(null);

    final Restaurants result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertSame(typeDomain, result.getRestaurantsType());
    Assertions.assertEquals(List.of(), result.getOpeningHours());
    Assertions.assertNull(result.getAddress());
    Assertions.assertNull(result.getUsers());

    Mockito.verify(restaurantsTypeMapper)
        .toDomain(typeEntity);
    Mockito.verifyNoInteractions(restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve lançar BadRequestException quando restaurantsType for null (validação do domínio)")
  void toDomainShouldThrowBadRequestExceptionWhenRestaurantsTypeIsNull() {
    final RestaurantsEntity entity = Mockito.mock(RestaurantsEntity.class);

    Mockito.when(entity.getUuid())
        .thenReturn(UUID.randomUUID());
    Mockito.when(entity.getName())
        .thenReturn("RESTAURANT");
    Mockito.when(entity.getRestaurantsType())
        .thenReturn(null);

    // crucial: evita NPE no stream()
    Mockito.when(entity.getOpeningHours())
        .thenReturn(Set.of());

    Mockito.when(entity.getAddress())
        .thenReturn(null);
    Mockito.when(entity.getUsers())
        .thenReturn(null);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> mapper.toDomain(entity)
    );

    Assertions.assertEquals("Restaurant type is required", exception.getMessage());
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando model for null no toEntity (novo)")
  void toEntityShouldReturnNullWhenModelIsNull() {
    final RestaurantsEntity result = mapper.toEntity(null, Mockito.mock(RestaurantsTypeEntity.class));

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve mapear model para entity com restaurantsType informado (novo)")
  void toEntityShouldMapModelToEntityWithRestaurantsType() {
    final var uuid = UUID.randomUUID();

    final Restaurants model = Mockito.mock(Restaurants.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("RESTAURANT");

    final RestaurantsTypeEntity typeEntity = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantsEntity result = mapper.toEntity(model, typeEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("RESTAURANT", result.getName());
    Assertions.assertSame(typeEntity, result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model (update)")
  void toEntityWithExistingEntityShouldUpdateFields() {
    final var entity = new RestaurantsEntity();
    entity.setUuid(UUID.randomUUID());
    entity.setName("OLD_NAME");
    entity.setRestaurantsType(Mockito.mock(RestaurantsTypeEntity.class));

    final Restaurants model = Mockito.mock(Restaurants.class);
    Mockito.when(model.getName())
        .thenReturn("NEW_NAME");

    final RestaurantsTypeEntity newTypeEntity = Mockito.mock(RestaurantsTypeEntity.class);

    final RestaurantsEntity result = mapper.toEntity(model, entity, newTypeEntity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertSame(newTypeEntity, result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }
}
