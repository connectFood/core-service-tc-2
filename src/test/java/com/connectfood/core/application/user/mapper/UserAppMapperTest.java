package com.connectfood.core.application.user.mapper;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.user.dto.UserInput;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.application.usertype.mapper.UserTypeAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserAppMapperTest {

  @Mock
  private UserTypeAppMapper usersTypeMapper;

  @Mock
  private AddressAppMapper addressMapper;

  @InjectMocks
  private UserAppMapper mapper;

  @Test
  @DisplayName("Deve retornar null no toDomain (sem UUID) quando input for nulo")
  void shouldReturnNullWhenInputIsNull() {
    final var result = mapper.toDomain(null, "hash", Mockito.mock(UserType.class));
    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter input para domínio corretamente (sem UUID)")
  void shouldMapInputToDomain() {
    final var input = Mockito.mock(UserInput.class);
    Mockito.when(input.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(input.getEmail())
        .thenReturn("pilar@test.com");

    final var passwordHash = "hashSeguro";
    final UserType userType = Mockito.mock(UserType.class);

    final var result = mapper.toDomain(input, passwordHash, userType);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertEquals(passwordHash, result.getPasswordHash());
    Assertions.assertSame(userType, result.getUserType());

    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve retornar null no toDomain (com UUID) quando input for nulo")
  void shouldReturnNullWhenInputWithUuidIsNull() {
    final var result = mapper.toDomain(UUID.randomUUID(), null, "hash", Mockito.mock(UserType.class));
    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter input e UUID para domínio corretamente")
  void shouldMapInputAndUuidToDomain() {
    final var uuid = UUID.randomUUID();

    final var input = Mockito.mock(UserInput.class);
    Mockito.when(input.getFullName())
        .thenReturn("Pilar Atualizada");
    Mockito.when(input.getEmail())
        .thenReturn("novo@test.com");

    final var passwordHash = "hashNovo";
    final UserType userType = Mockito.mock(UserType.class);

    final var result = mapper.toDomain(uuid, input, passwordHash, userType);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Pilar Atualizada", result.getFullName());
    Assertions.assertEquals("novo@test.com", result.getEmail());
    Assertions.assertEquals(passwordHash, result.getPasswordHash());
    Assertions.assertSame(userType, result.getUserType());

    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve retornar null no toOutput simples quando model for nulo")
  void shouldReturnNullWhenModelIsNull() {
    Assertions.assertNull(mapper.toOutput((User) null));
    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter domínio para output corretamente quando usersType for nulo")
  void shouldMapDomainToOutputWithoutUsersType() {
    final User model = Mockito.mock(User.class);
    final var uuid = UUID.randomUUID();

    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(model.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(model.getUserType())
        .thenReturn(null);

    final UserOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertNull(result.getUsersType());
    Assertions.assertNull(result.getAddress());

    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter domínio para output corretamente (sem endereço)")
  void shouldMapDomainToOutput() {
    final User model = Mockito.mock(User.class);
    final UserType userType = Mockito.mock(UserType.class);

    final var uuid = UUID.randomUUID();
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(model.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(model.getUserType())
        .thenReturn(userType);

    final UserTypeOutput userTypeOutput = Mockito.mock(UserTypeOutput.class);
    Mockito.when(usersTypeMapper.toOutput(userType))
        .thenReturn(userTypeOutput);

    final UserOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertSame(userTypeOutput, result.getUsersType());
    Assertions.assertNull(result.getAddress());

    Mockito.verify(usersTypeMapper, Mockito.times(1))
        .toOutput(userType);
    Mockito.verifyNoInteractions(addressMapper);
    Mockito.verifyNoMoreInteractions(usersTypeMapper);
  }

  @Test
  @DisplayName("Deve retornar null no toOutput(model, AddressOutput) quando model for nulo")
  void shouldReturnNullWhenModelIsNullForOutputWithAddressOutput() {
    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);

    final var result = mapper.toOutput(null, addressOutput);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter domínio para output com AddressOutput (usersType nulo)")
  void shouldMapDomainToOutputWithAddressOutputWithoutUsersType() {
    final User model = Mockito.mock(User.class);
    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);

    final var uuid = UUID.randomUUID();
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(model.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(model.getUserType())
        .thenReturn(null);

    final UserOutput result = mapper.toOutput(model, addressOutput);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertNull(result.getUsersType());
    Assertions.assertSame(addressOutput, result.getAddress());

    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter domínio para output com AddressOutput (usersType presente)")
  void shouldMapDomainToOutputWithAddressOutput() {
    final User model = Mockito.mock(User.class);
    final UserType userType = Mockito.mock(UserType.class);
    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);

    final var uuid = UUID.randomUUID();
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(model.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(model.getUserType())
        .thenReturn(userType);

    final UserTypeOutput userTypeOutput = Mockito.mock(UserTypeOutput.class);
    Mockito.when(usersTypeMapper.toOutput(userType))
        .thenReturn(userTypeOutput);

    final UserOutput result = mapper.toOutput(model, addressOutput);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertSame(userTypeOutput, result.getUsersType());
    Assertions.assertSame(addressOutput, result.getAddress());

    Mockito.verify(usersTypeMapper, Mockito.times(1))
        .toOutput(userType);
    Mockito.verifyNoInteractions(addressMapper);
    Mockito.verifyNoMoreInteractions(usersTypeMapper);
  }

  @Test
  @DisplayName("Deve retornar null no toOutput(model, Address) quando model ou address for nulo")
  void shouldReturnNullWhenModelOrAddressIsNull() {
    final User user = Mockito.mock(User.class);
    final Address address = Mockito.mock(Address.class);

    Assertions.assertNull(mapper.toOutput(null, address));
    Assertions.assertNull(mapper.toOutput(user, (Address) null));

    Mockito.verifyNoInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter domínio e Address para output corretamente (usersType presente)")
  void shouldMapDomainAndAddressToOutput() {
    final User model = Mockito.mock(User.class);
    final UserType userType = Mockito.mock(UserType.class);
    final Address address = Mockito.mock(Address.class);

    final var uuid = UUID.randomUUID();
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(model.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(model.getUserType())
        .thenReturn(userType);

    final UserTypeOutput userTypeOutput = Mockito.mock(UserTypeOutput.class);
    Mockito.when(usersTypeMapper.toOutput(userType))
        .thenReturn(userTypeOutput);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(addressMapper.toOutput(address))
        .thenReturn(addressOutput);

    final UserOutput result = mapper.toOutput(model, address);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertSame(userTypeOutput, result.getUsersType());
    Assertions.assertSame(addressOutput, result.getAddress());

    Mockito.verify(usersTypeMapper, Mockito.times(1))
        .toOutput(userType);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toOutput(address);
    Mockito.verifyNoMoreInteractions(usersTypeMapper, addressMapper);
  }

  @Test
  @DisplayName("Deve converter domínio e Address para output corretamente quando usersType for nulo")
  void shouldMapDomainAndAddressToOutputWithoutUsersType() {
    final User model = Mockito.mock(User.class);
    final Address address = Mockito.mock(Address.class);

    final var uuid = UUID.randomUUID();
    Mockito.when(model.getUuid())
        .thenReturn(uuid);
    Mockito.when(model.getFullName())
        .thenReturn("Maria Pilar");
    Mockito.when(model.getEmail())
        .thenReturn("pilar@test.com");
    Mockito.when(model.getUserType())
        .thenReturn(null);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(addressMapper.toOutput(address))
        .thenReturn(addressOutput);

    final UserOutput result = mapper.toOutput(model, address);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Maria Pilar", result.getFullName());
    Assertions.assertEquals("pilar@test.com", result.getEmail());
    Assertions.assertNull(result.getUsersType());
    Assertions.assertSame(addressOutput, result.getAddress());

    Mockito.verifyNoInteractions(usersTypeMapper);
    Mockito.verify(addressMapper, Mockito.times(1))
        .toOutput(address);
    Mockito.verifyNoMoreInteractions(addressMapper);
  }
}
