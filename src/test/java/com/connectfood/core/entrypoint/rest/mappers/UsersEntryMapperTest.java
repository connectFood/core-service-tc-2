package com.connectfood.core.entrypoint.rest.mappers;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.entrypoint.rest.dto.address.AddressRequest;
import com.connectfood.core.entrypoint.rest.dto.address.AddressResponse;
import com.connectfood.core.entrypoint.rest.dto.users.UsersRequest;
import com.connectfood.core.entrypoint.rest.dto.users.UsersResponse;
import com.connectfood.core.entrypoint.rest.dto.userstype.UsersTypeResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersEntryMapperTest {

  @Mock
  private UsersTypeEntryMapper usersTypeMapper;

  @Mock
  private AddressEntryMapper addressMapper;

  @InjectMocks
  private UsersEntryMapper mapper;

  @Test
  @DisplayName("Não deve converter para input quando request for null")
  void shouldReturnNullWhenRequestIsNull() {
    final UsersInput result = mapper.toInput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido e address for null")
  void shouldConvertToInputWhenRequestIsProvidedAndAddressIsNull() {
    final var usersTypeUuid = UUID.randomUUID();

    final UsersRequest request = Mockito.mock(UsersRequest.class);
    Mockito.when(request.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(request.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(request.getPassword())
        .thenReturn("senha123");
    Mockito.when(request.getUsersTypeUuid())
        .thenReturn(usersTypeUuid);
    Mockito.when(request.getAddress())
        .thenReturn(null);

    final UsersInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertEquals("senha123", result.getPassword());
    Assertions.assertEquals(usersTypeUuid, result.getUsersTypeUuid());
    Assertions.assertNull(result.getAddress());

    Mockito.verifyNoInteractions(usersTypeMapper);
    Mockito.verifyNoInteractions(addressMapper);
  }

  @Test
  @DisplayName("Deve converter para input quando request estiver preenchido e address estiver presente")
  void shouldConvertToInputWhenRequestIsProvidedAndAddressIsPresent() {
    final var usersTypeUuid = UUID.randomUUID();

    final AddressRequest addressRequest = Mockito.mock(AddressRequest.class);
    final AddressInput addressInput = Mockito.mock(AddressInput.class);
    Mockito.when(addressMapper.toInput(addressRequest))
        .thenReturn(addressInput);

    final UsersRequest request = Mockito.mock(UsersRequest.class);
    Mockito.when(request.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(request.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(request.getPassword())
        .thenReturn("senha123");
    Mockito.when(request.getUsersTypeUuid())
        .thenReturn(usersTypeUuid);
    Mockito.when(request.getAddress())
        .thenReturn(addressRequest);

    final UsersInput result = mapper.toInput(request);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertEquals("senha123", result.getPassword());
    Assertions.assertEquals(usersTypeUuid, result.getUsersTypeUuid());
    Assertions.assertSame(addressInput, result.getAddress());

    Mockito.verify(addressMapper, Mockito.times(1))
        .toInput(addressRequest);
    Mockito.verifyNoInteractions(usersTypeMapper);
    Mockito.verifyNoMoreInteractions(addressMapper);
  }

  @Test
  @DisplayName("Não deve converter para response quando output for null")
  void shouldReturnNullWhenOutputIsNull() {
    final UsersResponse result = mapper.toResponse(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando usersType e address forem null")
  void shouldConvertToResponseWhenUsersTypeAndAddressAreNull() {
    final var uuid = UUID.randomUUID();

    final UsersOutput output = Mockito.mock(UsersOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getFullName())
        .thenReturn("User A");
    Mockito.when(output.getEmail())
        .thenReturn("a@test.com");
    Mockito.when(output.getUsersType())
        .thenReturn(null);
    Mockito.when(output.getAddress())
        .thenReturn(null);

    final UsersResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("User A", result.getFullName());
    Assertions.assertEquals("a@test.com", result.getEmail());
    Assertions.assertNull(result.getUsersType());
    Assertions.assertNull(result.getAddress());

    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando usersType estiver presente e address for null")
  void shouldConvertToResponseWhenUsersTypeIsPresentAndAddressIsNull() {
    final var uuid = UUID.randomUUID();

    final UsersTypeOutput usersTypeOutput = Mockito.mock(UsersTypeOutput.class);
    final UsersTypeResponse usersTypeResponse = Mockito.mock(UsersTypeResponse.class);
    Mockito.when(usersTypeMapper.toResponse(usersTypeOutput))
        .thenReturn(usersTypeResponse);

    final UsersOutput output = Mockito.mock(UsersOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getFullName())
        .thenReturn("User B");
    Mockito.when(output.getEmail())
        .thenReturn("b@test.com");
    Mockito.when(output.getUsersType())
        .thenReturn(usersTypeOutput);
    Mockito.when(output.getAddress())
        .thenReturn(null);

    final UsersResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("User B", result.getFullName());
    Assertions.assertEquals("b@test.com", result.getEmail());
    Assertions.assertSame(usersTypeResponse, result.getUsersType());
    Assertions.assertNull(result.getAddress());

    Mockito.verify(usersTypeMapper, Mockito.times(1))
        .toResponse(usersTypeOutput);
    Mockito.verifyNoInteractions(addressMapper);
    Mockito.verifyNoMoreInteractions(usersTypeMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando usersType for null e address estiver presente")
  void shouldConvertToResponseWhenUsersTypeIsNullAndAddressIsPresent() {
    final var uuid = UUID.randomUUID();

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    final AddressResponse addressResponse = Mockito.mock(AddressResponse.class);
    Mockito.when(addressMapper.toResponse(addressOutput))
        .thenReturn(addressResponse);

    final UsersOutput output = Mockito.mock(UsersOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getFullName())
        .thenReturn("User C");
    Mockito.when(output.getEmail())
        .thenReturn("c@test.com");
    Mockito.when(output.getUsersType())
        .thenReturn(null);
    Mockito.when(output.getAddress())
        .thenReturn(addressOutput);

    final UsersResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("User C", result.getFullName());
    Assertions.assertEquals("c@test.com", result.getEmail());
    Assertions.assertNull(result.getUsersType());
    Assertions.assertSame(addressResponse, result.getAddress());

    Mockito.verify(addressMapper, Mockito.times(1))
        .toResponse(addressOutput);
    Mockito.verifyNoInteractions(usersTypeMapper);
    Mockito.verifyNoMoreInteractions(addressMapper);
  }

  @Test
  @DisplayName("Deve converter para response quando usersType e address estiverem presentes")
  void shouldConvertToResponseWhenUsersTypeAndAddressArePresent() {
    final var uuid = UUID.randomUUID();

    final UsersTypeOutput usersTypeOutput = Mockito.mock(UsersTypeOutput.class);
    final UsersTypeResponse usersTypeResponse = Mockito.mock(UsersTypeResponse.class);
    Mockito.when(usersTypeMapper.toResponse(usersTypeOutput))
        .thenReturn(usersTypeResponse);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    final AddressResponse addressResponse = Mockito.mock(AddressResponse.class);
    Mockito.when(addressMapper.toResponse(addressOutput))
        .thenReturn(addressResponse);

    final UsersOutput output = Mockito.mock(UsersOutput.class);
    Mockito.when(output.getUuid())
        .thenReturn(uuid);
    Mockito.when(output.getFullName())
        .thenReturn("User D");
    Mockito.when(output.getEmail())
        .thenReturn("d@test.com");
    Mockito.when(output.getUsersType())
        .thenReturn(usersTypeOutput);
    Mockito.when(output.getAddress())
        .thenReturn(addressOutput);

    final UsersResponse result = mapper.toResponse(output);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("User D", result.getFullName());
    Assertions.assertEquals("d@test.com", result.getEmail());
    Assertions.assertSame(usersTypeResponse, result.getUsersType());
    Assertions.assertSame(addressResponse, result.getAddress());

    Mockito.verify(usersTypeMapper, Mockito.times(1))
        .toResponse(usersTypeOutput);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toResponse(addressOutput);
    Mockito.verifyNoMoreInteractions(usersTypeMapper, addressMapper);
  }
}
