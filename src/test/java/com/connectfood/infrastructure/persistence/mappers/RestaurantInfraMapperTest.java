package com.connectfood.infrastructure.persistence.mappers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.RestaurantOpeningHour;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.User;
import com.connectfood.infrastructure.persistence.entity.AddressEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantOpeningHourEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.RestaurantTypeEntity;
import com.connectfood.infrastructure.persistence.entity.UserEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RestaurantInfraMapperTest {

  @Mock
  private RestaurantTypeInfraMapper restaurantsTypeMapper;

  @Mock
  private RestaurantOpeningHourInfraMapper restaurantOpeningHoursMapper;

  @Mock
  private AddressInfraMapper addressMapper;

  @Mock
  private UserInfraMapper usersMapper;

  @InjectMocks
  private RestaurantInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final Restaurant result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain com restaurantsType, openingHours, address e users")
  void toDomainShouldMapEntityToDomainWhenAllDataIsPresent() {
    final var uuid = UUID.randomUUID();
    final var name = "RESTAURANT";

    final RestaurantTypeEntity typeEntity = Mockito.mock(RestaurantTypeEntity.class);
    final RestaurantType typeDomain = Mockito.mock(RestaurantType.class);

    final RestaurantOpeningHourEntity ohEntity1 = Mockito.mock(RestaurantOpeningHourEntity.class);
    final RestaurantOpeningHourEntity ohEntity2 = Mockito.mock(RestaurantOpeningHourEntity.class);

    final RestaurantOpeningHour ohDomain1 = Mockito.mock(RestaurantOpeningHour.class);
    final RestaurantOpeningHour ohDomain2 = Mockito.mock(RestaurantOpeningHour.class);

    final AddressEntity addressEntity = Mockito.mock(AddressEntity.class);
    final Address addressDomain = Mockito.mock(Address.class);

    final UserEntity userEntity = Mockito.mock(UserEntity.class);
    final User userDomain = Mockito.mock(User.class);

    Mockito.when(restaurantsTypeMapper.toDomain(typeEntity))
        .thenReturn(typeDomain);
    Mockito.when(restaurantOpeningHoursMapper.toDomain(ohEntity1))
        .thenReturn(ohDomain1);
    Mockito.when(restaurantOpeningHoursMapper.toDomain(ohEntity2))
        .thenReturn(ohDomain2);
    Mockito.when(addressMapper.toDomain(addressEntity))
        .thenReturn(addressDomain);
    Mockito.when(usersMapper.toDomain(userEntity))
        .thenReturn(userDomain);

    final RestaurantEntity entity = Mockito.mock(RestaurantEntity.class);
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
        .thenReturn(userEntity);

    final Restaurant result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertSame(typeDomain, result.getRestaurantType());
    assertThat(result.getOpeningHours())
        .containsExactlyInAnyOrder(ohDomain1, ohDomain2);
    Assertions.assertSame(addressDomain, result.getAddress());
    Assertions.assertSame(userDomain, result.getUser());

    Mockito.verify(restaurantsTypeMapper)
        .toDomain(typeEntity);
    Mockito.verify(restaurantOpeningHoursMapper)
        .toDomain(ohEntity1);
    Mockito.verify(restaurantOpeningHoursMapper)
        .toDomain(ohEntity2);
    Mockito.verify(addressMapper)
        .toDomain(addressEntity);
    Mockito.verify(usersMapper)
        .toDomain(userEntity);
  }

  @Test
  @DisplayName("Deve mapear entity para domain com opcionais nulos e openingHours vazio")
  void toDomainShouldMapEntityToDomainWhenOptionalRelationsAreNull() {
    final var uuid = UUID.randomUUID();
    final var name = "RESTAURANT";

    final RestaurantTypeEntity typeEntity = Mockito.mock(RestaurantTypeEntity.class);
    final RestaurantType typeDomain = Mockito.mock(RestaurantType.class);

    Mockito.when(restaurantsTypeMapper.toDomain(typeEntity))
        .thenReturn(typeDomain);

    final RestaurantEntity entity = Mockito.mock(RestaurantEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getName())
        .thenReturn(name);
    Mockito.when(entity.getRestaurantsType())
        .thenReturn(typeEntity);

    Mockito.when(entity.getOpeningHours())
        .thenReturn(Set.of());

    Mockito.when(entity.getAddress())
        .thenReturn(null);
    Mockito.when(entity.getUsers())
        .thenReturn(null);

    final Restaurant result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertSame(typeDomain, result.getRestaurantType());
    Assertions.assertEquals(List.of(), result.getOpeningHours());
    Assertions.assertNull(result.getAddress());
    Assertions.assertNull(result.getUser());

    Mockito.verify(restaurantsTypeMapper)
        .toDomain(typeEntity);
    Mockito.verifyNoInteractions(restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve lançar BadRequestException quando restaurantsType for null (validação do domínio)")
  void toDomainShouldThrowBadRequestExceptionWhenRestaurantsTypeIsNull() {
    final RestaurantEntity entity = Mockito.mock(RestaurantEntity.class);

    Mockito.when(entity.getUuid())
        .thenReturn(UUID.randomUUID());
    Mockito.when(entity.getName())
        .thenReturn("RESTAURANT");
    Mockito.when(entity.getRestaurantsType())
        .thenReturn(null);

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
    final RestaurantEntity result = mapper.toEntity(null, Mockito.mock(RestaurantTypeEntity.class));

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve mapear model para entity com restaurantsType informado (novo)")
  void toEntityShouldMapModelToEntityWithRestaurantsType() {
    final var uuid = UUID.randomUUID();

    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("RESTAURANT");

    final RestaurantTypeEntity typeEntity = Mockito.mock(RestaurantTypeEntity.class);

    final RestaurantEntity result = mapper.toEntity(model, typeEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("RESTAURANT", result.getName());
    Assertions.assertSame(typeEntity, result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }

  @Test
  @DisplayName("Deve atualizar entity existente com base no model (update)")
  void toEntityWithExistingEntityShouldUpdateFields() {
    final var entity = new RestaurantEntity();
    entity.setUuid(UUID.randomUUID());
    entity.setName("OLD_NAME");
    entity.setRestaurantsType(Mockito.mock(RestaurantTypeEntity.class));

    final Restaurant model = Mockito.mock(Restaurant.class);
    Mockito.when(model.getName())
        .thenReturn("NEW_NAME");

    final RestaurantTypeEntity newTypeEntity = Mockito.mock(RestaurantTypeEntity.class);

    final RestaurantEntity result = mapper.toEntity(model, entity, newTypeEntity);

    Assertions.assertSame(entity, result);
    Assertions.assertEquals("NEW_NAME", result.getName());
    Assertions.assertSame(newTypeEntity, result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper, restaurantOpeningHoursMapper, addressMapper, usersMapper);
  }
}
