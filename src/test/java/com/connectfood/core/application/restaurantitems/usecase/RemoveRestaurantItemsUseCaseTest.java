package com.connectfood.core.application.restaurantitems.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItems;
import com.connectfood.core.domain.repository.RestaurantItemsRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantItemsUseCaseTest {

  @Mock
  private RestaurantItemsRepository repository;

  @Mock
  private RequestUserGuard guard;

  @InjectMocks
  private RemoveRestaurantItemsUseCase useCase;

  @Test
  @DisplayName("Deve remover item de restaurante quando ele existir")
  void shouldRemoveRestaurantItemsWhenExists() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var uuid = UUID.randomUUID();

    final RestaurantItems model = Mockito.mock(RestaurantItems.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(model));

    Assertions.assertDoesNotThrow(() -> useCase.execute(requestUser, uuid));

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .delete(uuid);

    Mockito.verifyNoMoreInteractions(guard, repository);
  }

  @Test
  @DisplayName("Não deve remover item de restaurante quando não existir e deve lançar NotFoundException")
  void shouldThrowNotFoundExceptionWhenRestaurantItemsDoesNotExist() {
    final var requestUserUuid = UUID.randomUUID();
    final var requestUser = new RequestUser(requestUserUuid);

    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(requestUser, uuid)
    );

    Assertions.assertEquals("Restaurant Items not found", exception.getMessage());

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(repository, Mockito.never())
        .delete(Mockito.any());

    Mockito.verifyNoMoreInteractions(guard, repository);
  }
}
