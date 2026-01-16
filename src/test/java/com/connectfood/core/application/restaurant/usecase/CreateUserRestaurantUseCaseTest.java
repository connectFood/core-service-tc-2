package com.connectfood.core.application.restaurant.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurant.mapper.UserRestaurantAppMapper;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserRestaurant;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserRestaurantGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserRestaurantUseCaseTest {

  @Mock
  private UserRestaurantGateway repository;

  @Mock
  private UserGateway userGateway;

  @Mock
  private RestaurantGateway restaurantGateway;

  @Mock
  private UserRestaurantAppMapper mapper;

  @Mock
  private UserAppMapper usersMapper;

  @InjectMocks
  private CreateUserRestaurantUseCase useCase;

  @Test
  @DisplayName("Deve lançar NotFoundException quando usuário não existir")
  void shouldThrowNotFoundExceptionWhenUserNotFound() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    Mockito.when(userGateway.findByUuid(usersUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(usersUuid, restaurantUuid)
    );

    Assertions.assertEquals("User not found", ex.getMessage());

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verifyNoInteractions(restaurantGateway, repository, mapper, usersMapper);
    Mockito.verifyNoMoreInteractions(userGateway);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando restaurante não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantNotFound() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final User user = Mockito.mock(User.class);
    Mockito.when(userGateway.findByUuid(usersUuid))
        .thenReturn(Optional.of(user));

    Mockito.when(restaurantGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(usersUuid, restaurantUuid)
    );

    Assertions.assertEquals("Restaurant not found", ex.getMessage());

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(restaurantGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verifyNoInteractions(repository, mapper, usersMapper);
    Mockito.verifyNoMoreInteractions(userGateway, restaurantGateway);
  }

  @Test
  @DisplayName("Deve criar vínculo usuário-restaurante e retornar UsersOutput do usuário vinculado")
  void shouldCreateUsersRestaurantAndReturnUsersOutput() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final User user = Mockito.mock(User.class);
    Mockito.when(userGateway.findByUuid(usersUuid))
        .thenReturn(Optional.of(user));

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    Mockito.when(restaurantGateway.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurant));

    final UserRestaurant userRestaurantDomain = Mockito.mock(UserRestaurant.class);
    Mockito.when(mapper.toDomain(user, restaurant))
        .thenReturn(userRestaurantDomain);

    final UserRestaurant savedUserRestaurant = Mockito.mock(UserRestaurant.class);
    Mockito.when(savedUserRestaurant.getUser())
        .thenReturn(user);
    Mockito.when(repository.save(userRestaurantDomain))
        .thenReturn(savedUserRestaurant);

    final UserOutput userOutput = Mockito.mock(UserOutput.class);
    Mockito.when(usersMapper.toOutput(user))
        .thenReturn(userOutput);

    final var result = useCase.execute(usersUuid, restaurantUuid);

    Assertions.assertNotNull(result);
    Assertions.assertSame(userOutput, result);

    Mockito.verify(userGateway, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(restaurantGateway, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(user, restaurant);
    Mockito.verify(repository, Mockito.times(1))
        .save(userRestaurantDomain);
    Mockito.verify(usersMapper, Mockito.times(1))
        .toOutput(user);

    Mockito.verifyNoMoreInteractions(
        userGateway,
        restaurantGateway,
        mapper,
        repository,
        usersMapper
    );
  }
}
