package com.connectfood.core.application.restaurants.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.repository.RestaurantsRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantsUseCaseTest {

  @Mock
  private RestaurantsRepository repository;

  @InjectMocks
  private RemoveRestaurantsUseCase useCase;

  @Test
  @DisplayName("Não deve remover restaurante quando uuid não existir")
  void shouldThrowExceptionWhenRestaurantsNotFound() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid)
    );

    Assertions.assertEquals("Restaurants not found", exception.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(repository, Mockito.never())
        .delete(Mockito.any());
  }

  @Test
  @DisplayName("Deve remover restaurante quando uuid existir")
  void shouldRemoveRestaurantsWhenUuidExists() {
    final var uuid = UUID.randomUUID();

    final Restaurants restaurants = Mockito.mock(Restaurants.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(restaurants));

    Assertions.assertDoesNotThrow(() -> useCase.execute(uuid));

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(repository, Mockito.times(1))
        .delete(uuid);
  }
}
