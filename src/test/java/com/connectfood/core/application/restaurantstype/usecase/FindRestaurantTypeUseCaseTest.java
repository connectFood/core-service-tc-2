package com.connectfood.core.application.restaurantstype.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.RestaurantsType;
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
class FindRestaurantTypeUseCaseTest {

  @Mock
  private RestaurantsTypeGateway repository;

  @Mock
  private RestaurantsTypeAppMapper mapper;

  @InjectMocks
  private FindRestaurantTypeUseCase useCase;

  @Test
  @DisplayName("Deve retornar o tipo de restaurante quando encontrado")
  void shouldReturnRestaurantsTypeWhenFound() {
    final var uuid = UUID.randomUUID();

    final RestaurantsType domain = Mockito.mock(RestaurantsType.class);
    final RestaurantsTypeOutput output = Mockito.mock(RestaurantsTypeOutput.class);

    Mockito.when(repository.findById(uuid))
        .thenReturn(Optional.of(domain));

    Mockito.when(mapper.toOutput(domain))
        .thenReturn(output);

    final var result = useCase.execute(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(repository, Mockito.times(1))
        .findById(uuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(domain);
    Mockito.verifyNoMoreInteractions(repository, mapper);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando tipo de restaurante não for encontrado")
  void shouldThrowNotFoundExceptionWhenRestaurantsTypeNotFound() {
    final var uuid = UUID.randomUUID();

    Mockito.when(repository.findById(uuid))
        .thenReturn(Optional.empty());

    final var exception = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(uuid)
    );

    Assertions.assertEquals("Restaurant type Not Found", exception.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findById(uuid);
    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(repository);
  }
}
