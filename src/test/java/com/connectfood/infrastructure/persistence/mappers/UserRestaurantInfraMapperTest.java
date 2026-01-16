package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserRestaurant;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.UserEntity;
import com.connectfood.infrastructure.persistence.entity.UserRestaurantEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRestaurantInfraMapperTest {

  @Mock
  private UserInfraMapper usersMapper;

  @Mock
  private RestaurantInfraMapper restaurantsMapper;

  @InjectMocks
  private UserRestaurantInfraMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando entity for null")
  void toDomainShouldReturnNullWhenEntityIsNull() {
    final UserRestaurant result = mapper.toDomain(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, restaurantsMapper);
  }

  @Test
  @DisplayName("Deve mapear entity para domain corretamente")
  void toDomainShouldMapEntityToDomainCorrectly() {
    final var uuid = UUID.randomUUID();

    final UserEntity userEntity = Mockito.mock(UserEntity.class);
    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);

    final User userDomain = Mockito.mock(User.class);
    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);

    Mockito.when(usersMapper.toDomain(userEntity))
        .thenReturn(userDomain);
    Mockito.when(restaurantsMapper.toDomain(restaurantEntity))
        .thenReturn(restaurantDomain);

    final UserRestaurantEntity entity = Mockito.mock(UserRestaurantEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getUsers())
        .thenReturn(userEntity);
    Mockito.when(entity.getRestaurants())
        .thenReturn(restaurantEntity);

    final UserRestaurant result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(userDomain, result.getUser());
    Assertions.assertSame(restaurantDomain, result.getRestaurant());

    Mockito.verify(usersMapper, Mockito.times(1))
        .toDomain(userEntity);
    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toDomain(restaurantEntity);
  }

  @Test
  @DisplayName("Deve retornar null quando usersEntity for null no toEntity")
  void toEntityShouldReturnNullWhenUsersEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);

    final UserRestaurantEntity result = mapper.toEntity(uuid, null, restaurantEntity);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, restaurantsMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsEntity for null no toEntity")
  void toEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final UserEntity userEntity = Mockito.mock(UserEntity.class);

    final UserRestaurantEntity result = mapper.toEntity(uuid, userEntity, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, restaurantsMapper);
  }

  @Test
  @DisplayName("Deve mapear para entity corretamente quando usersEntity e restaurantsEntity forem informados")
  void toEntityShouldMapToEntityCorrectly() {
    final var uuid = UUID.randomUUID();
    final UserEntity userEntity = Mockito.mock(UserEntity.class);
    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);

    final UserRestaurantEntity result = mapper.toEntity(uuid, userEntity, restaurantEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(userEntity, result.getUsers());
    Assertions.assertSame(restaurantEntity, result.getRestaurants());

    Mockito.verifyNoInteractions(usersMapper, restaurantsMapper);
  }
}
