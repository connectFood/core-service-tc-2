package com.connectfood.core.application.restaurantstype.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantsType;
import com.connectfood.core.domain.repository.RestaurantsTypeRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantTypeUseCaseTest {

  @Mock
  private RestaurantsTypeRepository repository;

  @InjectMocks
  private RemoveRestaurantTypeUseCase useCase;

  @Test
  @DisplayName("Deve remover o tipo de restaurante quando existir")
  void shouldRemoveRestaurantsTypeWhenExists() {
    final var uuid = UUID.randomUUID();
    final RestaurantsType domain = Mockito.mock(RestaurantsType.class);

    Mockito.when(repository.findById(uuid))
        .thenReturn(Optional.of(domain));

    Assertions.assertDoesNotThrow(() -> useCase.execute(uuid));

    Mockito.verify(repository, Mockito.times(1))
        .findById(uuid);
    Mockito.verify(repository, Mockito.times(1))
        .delete(uuid);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando tipo de restaurante não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantsTypeDoesNotExist() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findById(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid)
    );

    Assertions.assertEquals(
        "Restaurants Type not found: " + uuid,
        exception.getMessage()
    );

    Mockito.verify(repository, Mockito.times(1))
        .findById(uuid);
    Mockito.verify(repository, Mockito.never())
        .delete(Mockito.any());
    Mockito.verifyNoMoreInteractions(repository);
  }
}
