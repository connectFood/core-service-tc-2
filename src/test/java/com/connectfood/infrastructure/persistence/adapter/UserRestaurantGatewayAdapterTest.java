package com.connectfood.infrastructure.persistence.adapter;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserRestaurant;
import com.connectfood.infrastructure.persistence.entity.RestaurantEntity;
import com.connectfood.infrastructure.persistence.entity.UserEntity;
import com.connectfood.infrastructure.persistence.entity.UserRestaurantEntity;
import com.connectfood.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUserRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUserRestaurantRepository;
import com.connectfood.infrastructure.persistence.mappers.UserRestaurantInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRestaurantGatewayAdapterTest {

  @Mock
  private JpaUserRestaurantRepository repository;

  @Mock
  private UserRestaurantInfraMapper mapper;

  @Mock
  private JpaUserRepository usersRepository;

  @Mock
  private JpaRestaurantRepository restaurantsRepository;

  @InjectMocks
  private UserRestaurantGatewayAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar UsersRestaurant mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final var usersRestaurantUuid = UUID.randomUUID();
    final var userUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final User userDomain = Mockito.mock(User.class);
    Mockito.when(userDomain.getUuid())
        .thenReturn(userUuid);

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);

    final UserRestaurant userRestaurant = Mockito.mock(UserRestaurant.class);
    Mockito.when(userRestaurant.getUuid())
        .thenReturn(usersRestaurantUuid);
    Mockito.when(userRestaurant.getUser())
        .thenReturn(userDomain);
    Mockito.when(userRestaurant.getRestaurant())
        .thenReturn(restaurantDomain);

    final UserEntity userEntity = Mockito.mock(UserEntity.class);
    final RestaurantEntity restaurantEntity = Mockito.mock(RestaurantEntity.class);

    final UserRestaurantEntity entityToSave = Mockito.mock(UserRestaurantEntity.class);
    final UserRestaurantEntity savedEntity = Mockito.mock(UserRestaurantEntity.class);

    final UserRestaurant mappedDomain = Mockito.mock(UserRestaurant.class);

    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.of(userEntity));
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurantEntity));
    Mockito.when(mapper.toEntity(usersRestaurantUuid, userEntity, restaurantEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final UserRestaurant result = adapter.save(userRestaurant);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(usersRestaurantUuid, userEntity, restaurantEntity);
    Mockito.verify(repository, Mockito.times(1))
        .save(entityToSave);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(savedEntity);
    Mockito.verifyNoMoreInteractions(repository, mapper, usersRepository, restaurantsRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando user não existir ao salvar")
  void saveShouldThrowWhenUserDoesNotExist() {
    final var userUuid = UUID.randomUUID();

    final User userDomain = Mockito.mock(User.class);
    Mockito.when(userDomain.getUuid())
        .thenReturn(userUuid);


    final UserRestaurant userRestaurant = Mockito.mock(UserRestaurant.class);
    Mockito.when(userRestaurant.getUser())
        .thenReturn(userDomain);

    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(userRestaurant));

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verifyNoInteractions(restaurantsRepository, repository, mapper);
    Mockito.verifyNoMoreInteractions(usersRepository);
  }

  @Test
  @DisplayName("Deve lançar exceção quando restaurant não existir ao salvar")
  void saveShouldThrowWhenRestaurantDoesNotExist() {
    final var usersRestaurantUuid = UUID.randomUUID();
    final var userUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final User userDomain = Mockito.mock(User.class);
    Mockito.when(userDomain.getUuid())
        .thenReturn(userUuid);

    final Restaurant restaurantDomain = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);

    final UserRestaurant userRestaurant = Mockito.mock(UserRestaurant.class);
    Mockito.when(userRestaurant.getUser())
        .thenReturn(userDomain);
    Mockito.when(userRestaurant.getRestaurant())
        .thenReturn(restaurantDomain);

    final UserEntity userEntity = Mockito.mock(UserEntity.class);

    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.of(userEntity));
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(userRestaurant));

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(usersRepository, restaurantsRepository);
  }
}
