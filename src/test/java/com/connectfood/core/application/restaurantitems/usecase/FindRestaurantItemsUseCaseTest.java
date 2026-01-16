package com.connectfood.core.application.restaurantitems.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantitems.dto.RestaurantItemsOutput;
import com.connectfood.core.application.restaurantitems.mapper.RestaurantItemsAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantItem;
import com.connectfood.core.domain.repository.RestaurantItemGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindRestaurantItemsUseCaseTest {

  @Mock
  private RestaurantItemGateway repository;

  @Mock
  private RestaurantItemsAppMapper mapper;

  @InjectMocks
  private FindRestaurantItemsUseCase useCase;

  @Test
  @DisplayName("Deve retornar RestaurantItemsOutput quando item existir")
  void shouldReturnOutputWhenItemExists() {
    final var uuid = UUID.randomUUID();

    final RestaurantItem model = Mockito.mock(RestaurantItem.class);
    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(model));

    final RestaurantItemsOutput expectedOutput = Mockito.mock(RestaurantItemsOutput.class);
    Mockito.when(mapper.toOutput(model))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(uuid);

    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(model);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Não deve retornar RestaurantItemsOutput quando item não existir e deve lançar NotFoundException")
  void shouldThrowNotFoundExceptionWhenItemDoesNotExist() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid)
    );

    Assertions.assertEquals("Restaurant Items not found", exception.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verifyNoInteractions(mapper);
  }
}
