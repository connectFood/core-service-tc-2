package com.connectfood.core.application.restaurantstype.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantType;
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
class UpdateRestaurantTypeUseCaseTest {

  @Mock
  private RestaurantsTypeGateway repository;

  @Mock
  private RestaurantsTypeAppMapper mapper;

  @InjectMocks
  private UpdateRestaurantTypeUseCase useCase;

  @Test
  @DisplayName("Deve atualizar o tipo de restaurante quando existir e retornar output mapeado")
  void shouldUpdateRestaurantsTypeWhenExistsAndReturnOutput() {
    final var uuid = UUID.randomUUID();
    final var input = Mockito.mock(RestaurantsTypeInput.class);

    final RestaurantType existing = Mockito.mock(RestaurantType.class);
    final RestaurantType mappedDomain = Mockito.mock(RestaurantType.class);
    final RestaurantType updated = Mockito.mock(RestaurantType.class);
    final RestaurantsTypeOutput output = Mockito.mock(RestaurantsTypeOutput.class);

    Mockito.when(repository.findById(uuid))
        .thenReturn(Optional.of(existing));

    Mockito.when(mapper.toDomain(uuid, input))
        .thenReturn(mappedDomain);

    Mockito.when(repository.update(uuid, mappedDomain))
        .thenReturn(updated);

    Mockito.when(mapper.toOutput(updated))
        .thenReturn(output);

    final var result = useCase.execute(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(repository, Mockito.times(1))
        .findById(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(uuid, input);
    Mockito.verify(repository, Mockito.times(1))
        .update(uuid, mappedDomain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updated);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando tipo de restaurante não existir")
  void shouldThrowNotFoundExceptionWhenRestaurantsTypeDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final var input = Mockito.mock(RestaurantsTypeInput.class);

    Mockito.when(repository.findById(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid, input)
    );

    Assertions.assertEquals("Restaurant Type Not Found " + uuid, exception.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findById(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
