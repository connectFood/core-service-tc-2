package com.connectfood.core.entrypoint.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.address.dto.UsersAddressOutput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.entrypoint.rest.dto.address.UsersAddressResponse;
import com.connectfood.core.entrypoint.rest.dto.users.UsersResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersAddressEntryMapperTest {

  @Mock
  private UsersEntryMapper usersMapper;

  @InjectMocks
  private UsersAddressEntryMapper mapper;

  @Test
  @DisplayName("Não deve converter para UsersAddressResponse quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final UsersAddressResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper);
  }

  @Test
  @DisplayName("Deve converter para UsersAddressResponse quando users estiver presente")
  void shouldConvertToResponseWhenUsersIsPresent() {
    final var uuid = UUID.randomUUID();

    final UsersOutput usersOutput = Mockito.mock(UsersOutput.class);
    final UsersResponse usersResponse = Mockito.mock(UsersResponse.class);

    Mockito.when(usersMapper.toResponse(usersOutput))
        .thenReturn(usersResponse);

    final UsersAddressOutput output = Mockito.mock(UsersAddressOutput.class);
    Mockito.when(output.getUuid()).thenReturn(uuid);
    Mockito.when(output.getStreet()).thenReturn("Street");
    Mockito.when(output.getNumber()).thenReturn("123");
    Mockito.when(output.getComplement()).thenReturn("Apt 10");
    Mockito.when(output.getNeighborhood()).thenReturn("Downtown");
    Mockito.when(output.getCity()).thenReturn("City");
    Mockito.when(output.getState()).thenReturn("State");
    Mockito.when(output.getCountry()).thenReturn("Country");
    Mockito.when(output.getZipCode()).thenReturn("00000-000");
    Mockito.when(output.getUsers()).thenReturn(usersOutput);

    final UsersAddressResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Street", result.getStreet());
    Assertions.assertEquals("123", result.getNumber());
    Assertions.assertEquals("Apt 10", result.getComplement());
    Assertions.assertEquals("Downtown", result.getNeighborhood());
    Assertions.assertEquals("City", result.getCity());
    Assertions.assertEquals("State", result.getState());
    Assertions.assertEquals("Country", result.getCountry());
    Assertions.assertEquals("00000-000", result.getZipCode());
    Assertions.assertSame(usersResponse, result.getUsers());

    Mockito.verify(usersMapper, Mockito.times(1))
        .toResponse(usersOutput);
    Mockito.verifyNoMoreInteractions(usersMapper);
  }

  @Test
  @DisplayName("Deve converter para UsersAddressResponse quando users for null")
  void shouldConvertToResponseWhenUsersIsNull() {
    final var uuid = UUID.randomUUID();

    final UsersAddressOutput output = Mockito.mock(UsersAddressOutput.class);
    Mockito.when(output.getUuid()).thenReturn(uuid);
    Mockito.when(output.getStreet()).thenReturn("Street");
    Mockito.when(output.getNumber()).thenReturn("123");
    Mockito.when(output.getComplement()).thenReturn(null);
    Mockito.when(output.getNeighborhood()).thenReturn("Downtown");
    Mockito.when(output.getCity()).thenReturn("City");
    Mockito.when(output.getState()).thenReturn("State");
    Mockito.when(output.getCountry()).thenReturn("Country");
    Mockito.when(output.getZipCode()).thenReturn("00000-000");
    Mockito.when(output.getUsers()).thenReturn(null);

    final UsersAddressResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Street", result.getStreet());
    Assertions.assertEquals("123", result.getNumber());
    Assertions.assertNull(result.getComplement());
    Assertions.assertEquals("Downtown", result.getNeighborhood());
    Assertions.assertEquals("City", result.getCity());
    Assertions.assertEquals("State", result.getState());
    Assertions.assertEquals("Country", result.getCountry());
    Assertions.assertEquals("00000-000", result.getZipCode());
    Assertions.assertNull(result.getUsers());

    Mockito.verifyNoInteractions(usersMapper);
  }
}
