package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersRestaurant;
import com.connectfood.core.infrastructure.persistence.entity.RestaurantsEntity;
import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.core.infrastructure.persistence.entity.UsersRestaurantEntity;
import com.connectfood.core.infrastructure.persistence.jpa.JpaRestaurantsRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRestaurantRepository;
import com.connectfood.core.infrastructure.persistence.mappers.UsersRestaurantInfraMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersRestaurantRepositoryAdapterTest {

  @Mock
  private JpaUsersRestaurantRepository repository;

  @Mock
  private UsersRestaurantInfraMapper mapper;

  @Mock
  private JpaUsersRepository usersRepository;

  @Mock
  private JpaRestaurantsRepository restaurantsRepository;

  @InjectMocks
  private UsersRestaurantRepositoryAdapter adapter;

  @Test
  @DisplayName("Deve salvar e retornar UsersRestaurant mapeado")
  void saveShouldPersistAndReturnMappedModel() {
    final var usersRestaurantUuid = UUID.randomUUID();
    final var userUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final Users userDomain = Mockito.mock(Users.class);
    Mockito.when(userDomain.getUuid())
        .thenReturn(userUuid);

    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);

    final UsersRestaurant usersRestaurant = Mockito.mock(UsersRestaurant.class);
    Mockito.when(usersRestaurant.getUuid())
        .thenReturn(usersRestaurantUuid);
    Mockito.when(usersRestaurant.getUser())
        .thenReturn(userDomain);
    Mockito.when(usersRestaurant.getRestaurant())
        .thenReturn(restaurantDomain);

    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);
    final RestaurantsEntity restaurantsEntity = Mockito.mock(RestaurantsEntity.class);

    final UsersRestaurantEntity entityToSave = Mockito.mock(UsersRestaurantEntity.class);
    final UsersRestaurantEntity savedEntity = Mockito.mock(UsersRestaurantEntity.class);

    final UsersRestaurant mappedDomain = Mockito.mock(UsersRestaurant.class);

    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.of(usersEntity));
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurantsEntity));
    Mockito.when(mapper.toEntity(usersRestaurantUuid, usersEntity, restaurantsEntity))
        .thenReturn(entityToSave);
    Mockito.when(repository.save(entityToSave))
        .thenReturn(savedEntity);
    Mockito.when(mapper.toDomain(savedEntity))
        .thenReturn(mappedDomain);

    final UsersRestaurant result = adapter.save(usersRestaurant);

    Assertions.assertSame(mappedDomain, result);

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toEntity(usersRestaurantUuid, usersEntity, restaurantsEntity);
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

    final Users userDomain = Mockito.mock(Users.class);
    Mockito.when(userDomain.getUuid())
        .thenReturn(userUuid);


    final UsersRestaurant usersRestaurant = Mockito.mock(UsersRestaurant.class);
    Mockito.when(usersRestaurant.getUser())
        .thenReturn(userDomain);

    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(usersRestaurant));

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

    final Users userDomain = Mockito.mock(Users.class);
    Mockito.when(userDomain.getUuid())
        .thenReturn(userUuid);

    final Restaurants restaurantDomain = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantDomain.getUuid())
        .thenReturn(restaurantUuid);

    final UsersRestaurant usersRestaurant = Mockito.mock(UsersRestaurant.class);
    Mockito.when(usersRestaurant.getUser())
        .thenReturn(userDomain);
    Mockito.when(usersRestaurant.getRestaurant())
        .thenReturn(restaurantDomain);

    final UsersEntity usersEntity = Mockito.mock(UsersEntity.class);

    Mockito.when(usersRepository.findByUuid(userUuid))
        .thenReturn(Optional.of(usersEntity));
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    Assertions.assertThrows(NoSuchElementException.class, () -> adapter.save(usersRestaurant));

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verifyNoInteractions(repository, mapper);
    Mockito.verifyNoMoreInteractions(usersRepository, restaurantsRepository);
  }
}
