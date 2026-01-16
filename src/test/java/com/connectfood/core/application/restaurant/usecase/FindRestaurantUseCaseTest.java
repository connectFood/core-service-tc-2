package com.connectfood.core.application.restaurant.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.application.restaurant.mapper.RestaurantAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Restaurant;
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
class FindRestaurantUseCaseTest {

  @Mock
  private RestaurantGateway repository;

  @Mock
  private RestaurantAppMapper mapper;

  @InjectMocks
  private FindRestaurantUseCase useCase;

  @Test
  @DisplayName("Não deve retornar restaurante quando uuid não existir")
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
    Mockito.verifyNoInteractions(mapper);
  }

  @Test
  @DisplayName("Deve retornar restaurante quando uuid existir")
  void shouldReturnRestaurantsWhenUuidExists() {
    final var uuid = UUID.randomUUID();

    final Restaurant restaurant = Mockito.mock(Restaurant.class);
    final RestaurantOutput output = Mockito.mock(RestaurantOutput.class);

    Mockito.when(repository.findByUuid(uuid))
        .thenReturn(Optional.of(restaurant));

    Mockito.when(mapper.toOutput(restaurant))
        .thenReturn(output);

    final var result = useCase.execute(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(restaurant);
  }
}
