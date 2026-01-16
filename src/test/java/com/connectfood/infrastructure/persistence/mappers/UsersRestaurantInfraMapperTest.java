package com.connectfood.infrastructure.persistence.mappers;

import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserRestaurant;
import com.connectfood.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.infrastructure.persistence.entity.UsersRestaurantEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersRestaurantInfraMapperTest {

  @Mock
  private UsersInfraMapper usersMapper;

  @Mock
  private RestaurantsInfraMapper restaurantsMapper;

  @InjectMocks
  private UsersRestaurantInfraMapper mapper;

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

    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);
    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    final User userDomain = Mockito.mock(User.class);
    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);

    Mockito.when(usersMapper.toDomain(usersEntity))
        .thenReturn(userDomain);
    Mockito.when(restaurantsMapper.toDomain(restaurantsEntity))
        .thenReturn(restaurantDomain);

    final UsersRestaurantEntity entity = Mockito.mock(UsersRestaurantEntity.class);
    Mockito.when(entity.getUuid())
        .thenReturn(uuid);
    Mockito.when(entity.getUsers())
        .thenReturn(usersEntity);
    Mockito.when(entity.getRestaurants())
        .thenReturn(restaurantsEntity);

    final UserRestaurant result = mapper.toDomain(entity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(userDomain, result.getUser());
    Assertions.assertSame(restaurantDomain, result.getRestaurant());

    Mockito.verify(usersMapper, Mockito.times(1))
        .toDomain(usersEntity);
    Mockito.verify(restaurantsMapper, Mockito.times(1))
        .toDomain(restaurantsEntity);
  }

  @Test
  @DisplayName("Deve retornar null quando usersEntity for null no toEntity")
  void toEntityShouldReturnNullWhenUsersEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    final UsersRestaurantEntity result = mapper.toEntity(uuid, null, restaurantsEntity);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, restaurantsMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando restaurantsEntity for null no toEntity")
  void toEntityShouldReturnNullWhenRestaurantsEntityIsNull() {
    final var uuid = UUID.randomUUID();
    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);

    final UsersRestaurantEntity result = mapper.toEntity(uuid, usersEntity, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper, restaurantsMapper);
  }

  @Test
  @DisplayName("Deve mapear para entity corretamente quando usersEntity e restaurantsEntity forem informados")
  void toEntityShouldMapToEntityCorrectly() {
    final var uuid = UUID.randomUUID();
    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);
    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    final UsersRestaurantEntity result = mapper.toEntity(uuid, usersEntity, restaurantsEntity);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertSame(usersEntity, result.getUsers());
    Assertions.assertSame(restaurantsEntity, result.getRestaurants());

    Mockito.verifyNoInteractions(usersMapper, restaurantsMapper);
  }
}
