package com.connectfood.core.application.restaurants.mapper;

import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsInput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.restaurantstype.mapper.RestaurantsTypeAppMapper;
import com.connectfood.core.domain.model.Restaurants;
import com.connectfood.core.domain.model.RestaurantsType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantsAppMapperTest {

  @Mock
  private RestaurantsTypeAppMapper restaurantsTypeMapper;

  @InjectMocks
  private RestaurantsAppMapper mapper;

  @Test
  @DisplayName("Não deve criar domínio quando input for nulo")
  void shouldReturnNullWhenInputIsNull() {
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var result = mapper.toDomain((RestaurantsInput) null, restaurantsType);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve criar domínio a partir do input informado")
  void shouldCreateDomainFromInput() {
    final var input = new RestaurantsInput("Restaurant A", UUID.randomUUID());
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var result = mapper.toDomain(input, restaurantsType);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Restaurant A", result.getName());
    Assertions.assertSame(restaurantsType, result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Não deve criar domínio quando input for nulo mesmo com uuid informado")
  void shouldReturnNullWhenInputIsNullEvenWithUuid() {
    final var uuid = UUID.randomUUID();
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var result = mapper.toDomain(uuid, null, restaurantsType);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve criar domínio com uuid quando input estiver preenchido")
  void shouldCreateDomainWithUuidFromInput() {
    final var uuid = UUID.randomUUID();
    final var input = new RestaurantsInput("Restaurant B", UUID.randomUUID());
    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);

    final var result = mapper.toDomain(uuid, input, restaurantsType);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Restaurant B", result.getName());
    Assertions.assertSame(restaurantsType, result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Não deve criar output quando domínio for nulo")
  void shouldReturnNullWhenDomainIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }

  @Test
  @DisplayName("Deve criar output quando tipo de restaurante estiver presente")
  void shouldCreateOutputWhenRestaurantsTypeIsPresent() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurant C";

    final RestaurantsType restaurantsType = Mockito.mock(RestaurantsType.class);
    final var restaurantsTypeOutput = Mockito.mock(RestaurantsTypeOutput.class);

    Mockito.when(restaurantsTypeMapper.toOutput(restaurantsType))
        .thenReturn(restaurantsTypeOutput);

    final Restaurants model = Mockito.mock(Restaurants.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn(name);
    Mockito.when(model.getRestaurantsType())
        .thenReturn(restaurantsType);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertSame(restaurantsTypeOutput, result.getRestaurantsType());

    Mockito.verify(restaurantsTypeMapper, Mockito.times(1))
        .toOutput(restaurantsType);
  }

  @Test
  @DisplayName("Deve criar output sem tipo de restaurante quando não estiver presente")
  void shouldCreateOutputWithoutRestaurantsType() {
    final var uuid = UUID.randomUUID();
    final var name = "Restaurant D";

    final Restaurants model = Mockito.mock(Restaurants.class);
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getName())
        .thenReturn(name);
    Mockito.when(model.getRestaurantsType())
        .thenReturn(null);

    final var result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(name, result.getName());
    Assertions.assertNull(result.getRestaurantsType());

    Mockito.verifyNoInteractions(restaurantsTypeMapper);
  }
}
