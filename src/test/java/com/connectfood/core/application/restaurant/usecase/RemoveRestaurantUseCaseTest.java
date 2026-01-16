package com.connectfood.core.application.restaurant.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurant;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.RestaurantGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantUseCaseTest {

  @Mock
  private RestaurantGateway repository;

  @Mock
  private RequestUserGuard guard;

  @InjectMocks
  private RemoveRestaurantUseCase useCase;

  @Test
  @DisplayName("Não deve remover restaurante quando uuid não existir")
  void shouldThrowExceptionWhenRestaurantsNotFound() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, uuid)
    );

    Assertions.assertEquals("Restaurants not found", exception.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(repository, Mockito.never())
        .delete(Mockito.any());

    Mockito.verifyNoMoreInteractions(guard, repository);
  }

  @Test
  @DisplayName("Deve remover restaurante quando uuid existir")
  void shouldRemoveRestaurantsWhenUuidExists() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var uuid = UUID.randomUUID();

    final Restaurant restaurant = Mockito.mock(Restaurant.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(restaurant));

    Assertions.assertDoesNotThrow(() -> useCase.execute(requestUser, uuid));

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, UsersType.OWNER.name());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .delete(uuid);

    Mockito.verifyNoMoreInteractions(guard, repository);
  }
}
