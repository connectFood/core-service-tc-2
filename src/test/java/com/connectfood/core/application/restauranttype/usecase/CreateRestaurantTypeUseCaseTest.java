package com.connectfood.core.application.restauranttype.usecase;

import com.connectfood.core.application.restauranttype.dto.RestaurantTypeInput;
import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.application.restauranttype.mapper.RestaurantTypeAppMapper;
import com.connectfood.core.domain.exception.ConflictException;
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
  private RestaurantTypeAppMapper mapper;

  @InjectMocks
  private CreateRestaurantTypeUseCase useCase;

  @Test
  @DisplayName("Não deve criar tipo de restaurante quando já existir (ConflictException)")
  void shouldThrowConflictWhenRestaurantTypeAlreadyExists() {
    final var name = "Pizza";
    final var input = Mockito.mock(RestaurantTypeInput.class);

    Mockito.when(input.getName())
        .thenReturn(name);
    Mockito.when(repository.existsByName(name))
        .thenReturn(true);

    final var ex = Assertions.assertThrows(
        ConflictException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Restaurant type already exists", ex.getMessage());

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(repository, Mockito.times(1))
        .existsByName(name);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verifyNoMoreInteractions(input, repository);
  }

  @Test
  @DisplayName("Deve criar tipo de restaurante e retornar output mapeado")
  void shouldCreateRestaurantsTypeAndReturnOutput() {
    final var name = "Pizza";
    final var input = Mockito.mock(RestaurantTypeInput.class);

    Mockito.when(input.getName())
        .thenReturn(name);
    Mockito.when(repository.existsByName(name))
        .thenReturn(false);

    final RestaurantType domain = Mockito.mock(RestaurantType.class);
    final RestaurantType saved = Mockito.mock(RestaurantType.class);
    final RestaurantTypeOutput output = Mockito.mock(RestaurantTypeOutput.class);

    Mockito.when(mapper.toDomain(input))
        .thenReturn(domain);
    Mockito.when(repository.save(domain))
        .thenReturn(saved);
    Mockito.when(mapper.toOutput(saved))
        .thenReturn(output);

    final var result = useCase.execute(input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(output, result);

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(repository, Mockito.times(1))
        .existsByName(name);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(saved);

    Mockito.verifyNoMoreInteractions(input, mapper, repository);
  }

  @Test
  @DisplayName("Deve propagar exceção quando mapper.toDomain lançar erro (após validação passar)")
  void shouldThrowWhenMapperToDomainThrows() {
    final var name = "Pizza";
    final var input = Mockito.mock(RestaurantTypeInput.class);

    Mockito.when(input.getName())
        .thenReturn(name);
    Mockito.when(repository.existsByName(name))
        .thenReturn(false);

    Mockito.when(mapper.toDomain(input))
        .thenThrow(new RuntimeException("Mapper error"));

    final var ex = Assertions.assertThrows(
        RuntimeException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Mapper error", ex.getMessage());

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(repository, Mockito.times(1))
        .existsByName(name);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);

    Mockito.verifyNoMoreInteractions(input, mapper, repository);
  }

  @Test
  @DisplayName("Deve propagar exceção quando repository.save lançar erro (após validação passar)")
  void shouldThrowWhenRepositorySaveThrows() {
    final var name = "Pizza";
    final var input = Mockito.mock(RestaurantTypeInput.class);

    Mockito.when(input.getName())
        .thenReturn(name);
    Mockito.when(repository.existsByName(name))
        .thenReturn(false);

    final RestaurantType domain = Mockito.mock(RestaurantType.class);
    Mockito.when(mapper.toDomain(input))
        .thenReturn(domain);

    Mockito.when(repository.save(domain))
        .thenThrow(new RuntimeException("Repository error"));

    final var ex = Assertions.assertThrows(
        RuntimeException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Repository error", ex.getMessage());

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(repository, Mockito.times(1))
        .existsByName(name);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain);

    Mockito.verifyNoMoreInteractions(input, mapper, repository);
  }

  @Test
  @DisplayName("Deve retornar null quando mapper.toOutput retornar null")
  void shouldReturnNullWhenMapperToOutputReturnsNull() {
    final var name = "Pizza";
    final var input = Mockito.mock(RestaurantTypeInput.class);

    Mockito.when(input.getName())
        .thenReturn(name);
    Mockito.when(repository.existsByName(name))
        .thenReturn(false);

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

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(repository, Mockito.times(1))
        .existsByName(name);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input);
    Mockito.verify(repository, Mockito.times(1))
        .save(domain);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(saved);

    Mockito.verifyNoMoreInteractions(input, mapper, repository);
  }
}
