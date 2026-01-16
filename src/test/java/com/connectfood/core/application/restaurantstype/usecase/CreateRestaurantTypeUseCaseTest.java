package com.connectfood.core.application.restaurantstype.usecase;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.model.RestaurantType;
import com.connectfood.core.domain.repository.RestaurantTypeGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantTypeUseCaseTest {

  @Mock
  private RestaurantTypeGateway repository;

  @Mock
  private RestaurantsTypeAppMapper mapper;

  @InjectMocks
  private CreateRestaurantTypeUseCase useCase;

  @Test
  @DisplayName("Deve criar tipo de restaurante e retornar output mapeado")
  void shouldCreateRestaurantsTypeAndReturnOutput() {
    final var input = Mockito.mock(RestaurantsTypeInput.class);

    final RestaurantType domain = Mockito.mock(RestaurantType.class);
    final RestaurantType saved = Mockito.mock(RestaurantType.class);
    final RestaurantsTypeOutput output = Mockito.mock(RestaurantsTypeOutput.class);

    Mockito.when(mapper.toDomain(input))
        .thenReturn(domain);

    Mockito.when(repository.save(domain))
        .thenReturn(saved);

    Mockito.when(mapper.toOutput(saved))
        .thenReturn(output);

    final var result = useCase.execute(input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(saved);
    Mockito.verifyNoMoreInteractions(mapper, repository);
  }

  @Test
  @DisplayName("Deve propagar exceção quando mapper.toDomain lançar erro")
  void shouldThrowWhenMapperToDomainThrows() {
    final var input = Mockito.mock(RestaurantsTypeInput.class);

    Mockito.when(mapper.toDomain(input))
        .thenThrow(new RuntimeException("Mapper error"));

    final var ex = Assertions.assertThrows(RuntimeException.class, () -> useCase.execute(input));

    Assertions.assertEquals("Mapper error", ex.getMessage());

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verifyNoInteractions(repository);
    Mockito.verifyNoMoreInteractions(mapper);
  }

  @Test
  @DisplayName("Deve propagar exceção quando repository.save lançar erro")
  void shouldThrowWhenRepositorySaveThrows() {
    final var input = Mockito.mock(RestaurantsTypeInput.class);

    final RestaurantType domain = Mockito.mock(RestaurantType.class);

    Mockito.when(mapper.toDomain(input))
        .thenReturn(domain);

    Mockito.when(repository.save(domain))
        .thenThrow(new RuntimeException("Repository error"));

    final var ex = Assertions.assertThrows(RuntimeException.class, () -> useCase.execute(input));

    Assertions.assertEquals("Repository error", ex.getMessage());

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain);
    Mockito.verifyNoMoreInteractions(mapper, repository);
  }

  @Test
  @DisplayName("Deve retornar null quando mapper.toOutput retornar null")
  void shouldReturnNullWhenMapperToOutputReturnsNull() {
    final var input = Mockito.mock(RestaurantsTypeInput.class);

    final RestaurantType domain = Mockito.mock(RestaurantType.class);
    final RestaurantType saved = Mockito.mock(RestaurantType.class);

    Mockito.when(mapper.toDomain(input))
        .thenReturn(domain);

    Mockito.when(repository.save(domain))
        .thenReturn(saved);

    Mockito.when(mapper.toOutput(saved))
        .thenReturn(null);

    final var result = useCase.execute(input);

    Assertions.assertNull(result);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(saved);
    Mockito.verifyNoMoreInteractions(mapper, repository);
  }
}
