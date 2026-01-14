package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurants.mapper.UsersRestaurantAppMapper;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersRestaurant;
import com.connectfood.core.domain.repository.RestaurantsRepository;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.repository.UsersRestaurantRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUsersRestaurantUseCaseTest {

  @Mock
  private UsersRestaurantRepository repository;

  @Mock
  private UsersRepository usersRepository;

  @Mock
  private RestaurantsRepository restaurantsRepository;

  @Mock
  private UsersRestaurantAppMapper mapper;

  @Mock
  private UsersAppMapper usersMapper;

  @InjectMocks
  private CreateUsersRestaurantUseCase useCase;

  @Test
  @DisplayName("Deve lançar NotFoundException quando usuário não existir")
  void shouldThrowNotFoundExceptionWhenUserNotFound() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(usersUuid, restaurantUuid)
    );

    Assertions.assertEquals("User not found", ex.getMessage());

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verifyNoInteractions(restaurantsRepository, repository, mapper, usersMapper);
    Mockito.verifyNoMoreInteractions(usersRepository);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando restaurante não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantNotFound() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final Users users = Mockito.mock(Users.class);
    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.of(users));

    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(usersUuid, restaurantUuid)
    );

    Assertions.assertEquals("Restaurant not found", ex.getMessage());

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verifyNoInteractions(repository, mapper, usersMapper);
    Mockito.verifyNoMoreInteractions(usersRepository, restaurantsRepository);
  }

  @Test
  @DisplayName("Deve criar vínculo usuário-restaurante e retornar UsersOutput do usuário vinculado")
  void shouldCreateUsersRestaurantAndReturnUsersOutput() {
    final var usersUuid = UUID.randomUUID();
    final var restaurantUuid = UUID.randomUUID();

    final Users users = Mockito.mock(Users.class);
    Mockito.when(usersRepository.findByUuid(usersUuid))
        .thenReturn(Optional.of(users));

    final Restaurants restaurants = Mockito.mock(Restaurants.class);
    Mockito.when(restaurantsRepository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(restaurants));

    final UsersRestaurant usersRestaurantDomain = Mockito.mock(UsersRestaurant.class);
    Mockito.when(mapper.toDomain(users, restaurants))
        .thenReturn(usersRestaurantDomain);

    final UsersRestaurant savedUsersRestaurant = Mockito.mock(UsersRestaurant.class);
    Mockito.when(savedUsersRestaurant.getUser())
        .thenReturn(users);
    Mockito.when(repository.save(usersRestaurantDomain))
        .thenReturn(savedUsersRestaurant);

    final UsersOutput usersOutput = Mockito.mock(UsersOutput.class);
    Mockito.when(usersMapper.toOutput(users))
        .thenReturn(usersOutput);

    final var result = useCase.execute(usersUuid, restaurantUuid);

    Assertions.assertNotNull(result);
    Assertions.assertSame(usersOutput, result);

    Mockito.verify(usersRepository, Mockito.times(1))
        .findByUuid(usersUuid);
    Mockito.verify(restaurantsRepository, Mockito.times(1))
        .findByUuid(restaurantUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(users, restaurants);
    Mockito.verify(repository, Mockito.times(1))
        .save(usersRestaurantDomain);
    Mockito.verify(usersMapper, Mockito.times(1))
        .toOutput(users);

    Mockito.verifyNoMoreInteractions(
        usersRepository,
        restaurantsRepository,
        mapper,
        repository,
        usersMapper
    );
  }
}
