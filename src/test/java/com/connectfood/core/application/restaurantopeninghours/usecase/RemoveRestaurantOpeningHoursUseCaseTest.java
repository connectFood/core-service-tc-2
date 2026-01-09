package com.connectfood.core.application.restaurantopeninghours.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurants.usecase.RemoveRestaurantOpeningHoursUseCase;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantOpeningHours;
import com.connectfood.core.domain.repository.RestaurantOpeningHoursRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveRestaurantOpeningHoursUseCaseTest {

  @Mock
  private RestaurantOpeningHoursRepository repository;

  @InjectMocks
  private RemoveRestaurantOpeningHoursUseCase useCase;

  @Test
  @DisplayName("Deve remover horário de funcionamento quando registro existir")
  void shouldRemoveOpeningHoursWhenExists() {
    final var uuid = UUID.randomUUID();

    final RestaurantOpeningHours model = Mockito.mock(RestaurantOpeningHours.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(model));

    useCase.execute(uuid);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(repository, Mockito.times(1))
        .delete(uuid);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Não deve remover horário de funcionamento quando registro não existir")
  void shouldThrowNotFoundExceptionWhenDoesNotExist() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid)
    );

    Assertions.assertEquals(
        "Restaurant opening hours not found",
        exception.getMessage()
    );

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(repository, Mockito.never())
        .delete(Mockito.any());
    Mockito.verifyNoMoreInteractions(repository);
  }
}
