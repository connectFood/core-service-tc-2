package com.connectfood.core.application.restaurantstype.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.domain.model.RestaurantsType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsTypeAppMapperTest {

  @InjectMocks
  private RestaurantsTypeAppMapper mapper;

  @Test
  @DisplayName("Não deve criar domínio quando input for nulo")
  void shouldReturnNullWhenInputIsNull() {
    final var result = mapper.toDomain((RestaurantsTypeInput) null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar domínio a partir do input informado")
  void shouldCreateDomainFromInput() {
    final var input = Mockito.mock(RestaurantsTypeInput.class);
    Mockito.when(input.getName())
        .thenReturn("Fast Food");
    Mockito.when(input.getDescription())
        .thenReturn("Quick service restaurant");

    final var result = mapper.toDomain(input);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid(), "UUID deve ser gerado pelo domínio quando não informado");
    Assertions.assertEquals("Fast Food", result.getName());
    Assertions.assertEquals("Quick service restaurant", result.getDescription());

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(input, Mockito.times(1))
        .getDescription();
    Mockito.verifyNoMoreInteractions(input);
  }

  @Test
  @DisplayName("Não deve criar domínio quando input for nulo mesmo com uuid informado")
  void shouldReturnNullWhenInputIsNullEvenWithUuid() {
    final var uuid = UUID.randomUUID();

    final var result = mapper.toDomain(uuid, null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar domínio com uuid quando input estiver preenchido")
  void shouldCreateDomainWithUuidFromInput() {
    final var uuid = UUID.randomUUID();

    final var input = Mockito.mock(RestaurantsTypeInput.class);
    Mockito.when(input.getName())
        .thenReturn("Pizzaria");
    Mockito.when(input.getDescription())
        .thenReturn("Italian pizza");

    final var result = mapper.toDomain(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Pizzaria", result.getName());
    Assertions.assertEquals("Italian pizza", result.getDescription());

    Mockito.verify(input, Mockito.times(1))
        .getName();
    Mockito.verify(input, Mockito.times(1))
        .getDescription();
    Mockito.verifyNoMoreInteractions(input);
  }

  @Test
  @DisplayName("Não deve criar output quando domínio for nulo")
  void shouldReturnNullWhenDomainIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar output a partir do domínio informado")
  void shouldCreateOutputFromDomain() {
    final var uuid = UUID.randomUUID();

    final var model = Mockito.mock(RestaurantsType.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn("Vegetariano");
    Mockito.when(model.getDescription())
        .thenReturn("Vegetarian restaurant");

    final RestaurantsTypeOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Vegetariano", result.getName());
    Assertions.assertEquals("Vegetarian restaurant", result.getDescription());

    Mockito.verify(model, Mockito.times(1))
        .getUuid();
    Mockito.verify(model, Mockito.times(1))
        .getName();
    Mockito.verify(model, Mockito.times(1))
        .getDescription();
    Mockito.verifyNoMoreInteractions(model);
  }
}
