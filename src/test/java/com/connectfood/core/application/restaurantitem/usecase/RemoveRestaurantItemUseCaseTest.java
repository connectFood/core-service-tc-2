package com.connectfood.core.application.restaurantitem.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.core.domain.repository.RestaurantItemImageGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantItemUseCaseTest {

  @Mock
  private RestaurantItemGateway repository;

  @Mock
  private RequestUserGuard guard;

  @Mock
  private RestaurantItemImageGateway restaurantItemImageGateway;

  @InjectMocks
  private RemoveRestaurantItemUseCase useCase;

  @Test
  @DisplayName("Deve remover item de restaurante quando ele existir e não houver imagens")
  void shouldRemoveRestaurantItemsWhenExistsAndNoImages() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var uuid = UUID.randomUUID();

    final RestaurantItem model = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(model));

    Mockito.when(restaurantItemImageGateway.existsByRestaurantItemUuid(uuid))
        .thenReturn(false);

    Assertions.assertDoesNotThrow(() -> useCase.execute(requestUser, uuid));

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(restaurantItemImageGateway, Mockito.times(1))
        .existsByRestaurantItemUuid(uuid);

    Mockito.verify(restaurantItemImageGateway, Mockito.never())
        .deleteByRestaurantItemUuid(Mockito.any());

    Mockito.verify(repository, Mockito.times(1))
        .delete(uuid);

    Mockito.verifyNoMoreInteractions(guard, repository, restaurantItemImageGateway);
  }

  @Test
  @DisplayName("Deve remover item de restaurante quando ele existir e houver imagens (deleta imagens antes)")
  void shouldRemoveRestaurantItemsWhenExistsAndHasImages() {
    final var requestUser = new RequestUser(UUID.randomUUID());
    final var uuid = UUID.randomUUID();

    final RestaurantItem model = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(model));

    Mockito.when(restaurantItemImageGateway.existsByRestaurantItemUuid(uuid))
        .thenReturn(true);

    Assertions.assertDoesNotThrow(() -> useCase.execute(requestUser, uuid));

    Mockito.verify(guard, Mockito.times(1))
        .requireRole(requestUser, "OWNER");

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);

    Mockito.verify(restaurantItemImageGateway, Mockito.times(1))
        .existsByRestaurantItemUuid(uuid);

    Mockito.verify(restaurantItemImageGateway, Mockito.times(1))
        .deleteByRestaurantItemUuid(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .delete(uuid);

    Mockito.verifyNoMoreInteractions(guard, repository, restaurantItemImageGateway);
  }

  @Test
  @DisplayName("Não deve remover item de restaurante quando não existir e deve lançar NotFoundException")
  void shouldThrowNotFoundExceptionWhenRestaurantItemsDoesNotExist() {
    final var requestUser = new RequestUser(UUID.randomUUID());
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

    Mockito.verifyNoInteractions(restaurantItemImageGateway);

    Mockito.verify(repository, Mockito.never())
        .delete(Mockito.any());

    Mockito.verifyNoMoreInteractions(guard, repository);
  }
}
