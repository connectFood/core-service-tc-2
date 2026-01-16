package com.connectfood.infrastructure.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.infrastructure.rest.dto.userstype.UsersTypeRequest;
import com.connectfood.infrastructure.rest.dto.userstype.UsersTypeResponse;
import com.connectfood.infrastructure.rest.mappers.UsersTypeEntryMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersTypeEntryMapperTest {

  private final UsersTypeEntryMapper mapper = new UsersTypeEntryMapper();

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final UsersTypeInput result = mapper.toInput(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido")
  void shouldConvertToInputWhenRequestIsProvided() {
    final UsersTypeRequest request = org.mockito.Mockito.mock(UsersTypeRequest.class);

    org.mockito.Mockito.when(request.getName())
        .thenReturn("ADMIN");
    org.mockito.Mockito.when(request.getDescription())
        .thenReturn("Administrator");

    final UsersTypeInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("ADMIN", result.getName());
    Assertions.assertEquals("Administrator", result.getDescription());
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final UsersTypeResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve converter para response quando output estiver preenchido")
  void shouldConvertToResponseWhenOutputIsProvided() {
    final var uuid = UUID.randomUUID();

    final UsersTypeOutput output = org.mockito.Mockito.mock(UsersTypeOutput.class);
    org.mockito.Mockito.when(output.getUuid())
        .thenReturn(uuid);
    org.mockito.Mockito.when(output.getName())
        .thenReturn("CLIENT");
    org.mockito.Mockito.when(output.getDescription())
        .thenReturn("Customer");

    final UsersTypeResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("CLIENT", result.getName());
    Assertions.assertEquals("Customer", result.getDescription());
  }
}
