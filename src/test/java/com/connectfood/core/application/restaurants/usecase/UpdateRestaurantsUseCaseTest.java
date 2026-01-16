package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantsGateway;
import com.connectfood.core.domain.repository.RestaurantsTypeGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantsUseCaseTest {

  @Mock
  private RestaurantsGateway repository;

  @Mock
  private RestaurantsAppMapper mapper;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private RestaurantsTypeGateway restaurantsTypeGateway;

  @InjectMocks
  private UpdateRestaurantsUseCase useCase;

  @Test
  @DisplayName("Deve atualizar restaurante mantendo o tipo quando restaurantsTypeUuid for o mesmo")
  void shouldUpdateKeepingRestaurantsTypeWhenUuidIsTheSame() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final var restaurantsTypeUuid = UUID.randomUUID();

    final RestaurantType currentType = Mockito.mock(RestaurantType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(restaurantsTypeUuid);

    final Restaurant existingRestaurant = Mockito.mock(Restaurant.class);
    Mockito.when(existingRestaurant.getRestaurantType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(existingRestaurant));

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getRestaurantsTypeUuid())
        .thenReturn(restaurantsTypeUuid);

    final Restaurant mappedDomain = Mockito.mock(Restaurant.class);
    Mockito.when(mapper.toDomain(restaurantUuid, input, currentType))
        .thenReturn(mappedDomain);

    final Restaurant updatedRestaurant = Mockito.mock(Restaurant.class);
    Mockito.when(repository.update(restaurantUuid, mappedDomain))
        .thenReturn(updatedRestaurant);

    final RestaurantsOutput expectedOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(mapper.toOutput(updatedRestaurant))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    // Implementação chama getRestaurantsType() 2x (uma pra atribuir, outra pra comparar)
    Mockito.verify(existingRestaurant, Mockito.times(2))
        .getRestaurantType();

    Mockito.verify(currentType, Mockito.times(1))
        .getUuid();

    Mockito.verifyNoInteractions(restaurantsTypeGateway);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(restaurantUuid, input, currentType);

    Mockito.verify(repository, Mockito.times(1))
        .update(restaurantUuid, mappedDomain);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedRestaurant);

    Mockito.verifyNoMoreInteractions(
        guard, repository, mapper, existingRestaurant, currentType
    );
  }

  @Test
  @DisplayName("Deve atualizar restaurante buscando novo tipo quando restaurantsTypeUuid for diferente")
  void shouldUpdateFetchingNewRestaurantsTypeWhenUuidIsDifferent() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final var currentTypeUuid = UUID.randomUUID();
    final var newTypeUuid = UUID.randomUUID();

    final RestaurantType currentType = Mockito.mock(RestaurantType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final Restaurant existingRestaurant = Mockito.mock(Restaurant.class);
    Mockito.when(existingRestaurant.getRestaurantType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(existingRestaurant));

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getRestaurantsTypeUuid())
        .thenReturn(newTypeUuid);

    final RestaurantType newType = Mockito.mock(RestaurantType.class);
    Mockito.when(restaurantsTypeGateway.findById(newTypeUuid))
        .thenReturn(Optional.of(newType));

    final Restaurant mappedDomain = Mockito.mock(Restaurant.class);
    Mockito.when(mapper.toDomain(restaurantUuid, input, newType))
        .thenReturn(mappedDomain);

    final Restaurant updatedRestaurant = Mockito.mock(Restaurant.class);
    Mockito.when(repository.update(restaurantUuid, mappedDomain))
        .thenReturn(updatedRestaurant);

    final RestaurantsOutput expectedOutput = Mockito.mock(RestaurantsOutput.class);
    Mockito.when(mapper.toOutput(updatedRestaurant))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(requestUser, restaurantUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verify(existingRestaurant, Mockito.times(2))
        .getRestaurantType();

    Mockito.verify(currentType, Mockito.times(1))
        .getUuid();

    Mockito.verify(restaurantsTypeGateway, Mockito.times(1))
        .findById(newTypeUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(restaurantUuid, input, newType);

    Mockito.verify(repository, Mockito.times(1))
        .update(restaurantUuid, mappedDomain);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedRestaurant);

    Mockito.verifyNoMoreInteractions(
        guard, repository, mapper, restaurantsTypeGateway, existingRestaurant, currentType
    );
  }

  @Test
  @DisplayName("Não deve atualizar restaurante quando restaurante não existir")
  void shouldThrowWhenRestaurantDoesNotExist() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, restaurantUuid, input)
    );

    Assertions.assertEquals("Restaurants not found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verifyNoInteractions(restaurantsTypeGateway, mapper);

    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());

    Mockito.verifyNoMoreInteractions(guard, repository);
  }

  @Test
  @DisplayName("Não deve atualizar restaurante quando tipo informado não existir e uuid for diferente")
  void shouldThrowWhenRestaurantsTypeDoesNotExistAndUuidIsDifferent() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var restaurantUuid = UUID.randomUUID();
    final var currentTypeUuid = UUID.randomUUID();
    final var newTypeUuid = UUID.randomUUID();

    final RestaurantType currentType = Mockito.mock(RestaurantType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final Restaurant existingRestaurant = Mockito.mock(Restaurant.class);
    Mockito.when(existingRestaurant.getRestaurantType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(restaurantUuid))
        .thenReturn(Optional.of(existingRestaurant));

    final RestaurantsInput input = Mockito.mock(RestaurantsInput.class);
    Mockito.when(input.getRestaurantsTypeUuid())
        .thenReturn(newTypeUuid);

    Mockito.when(restaurantsTypeGateway.findById(newTypeUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, restaurantUuid, input)
    );

    Assertions.assertEquals("Restaurant type not found", ex.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(restaurantUuid);

    Mockito.verify(existingRestaurant, Mockito.times(2))
        .getRestaurantType();

    Mockito.verify(currentType, Mockito.times(1))
        .getUuid();

    Mockito.verify(restaurantsTypeGateway, Mockito.times(1))
        .findById(newTypeUuid);

    Mockito.verifyNoInteractions(mapper);

    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());

    Mockito.verifyNoMoreInteractions(
        guard, repository, restaurantsTypeGateway, existingRestaurant, currentType
    );
  }
}
