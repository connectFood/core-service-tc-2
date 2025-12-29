package com.connectfood.core.application.address.mapper;

import java.util.UUID;

import com.connectfood.core.application.address.dto.UsersAddressOutput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersAddress;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersAddressAppMapperTest {

  @Mock
  private UsersAppMapper usersMapper;

  @InjectMocks
  private UsersAddressAppMapper mapper;

  @Test
  @DisplayName("Deve retornar null quando users for null")
  void shouldReturnNullWhenUsersIsNull() {
    final Address address = Mockito.mock(Address.class);

    final var result = mapper.toDomain(null, address);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando address for null")
  void shouldReturnNullWhenAddressIsNull() {
    final Users users = Mockito.mock(Users.class);

    final var result = mapper.toDomain(users, null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper);
  }

  @Test
  @DisplayName("Deve mapear users e address para domain")
  void shouldMapUsersAndAddressToDomain() {
    final Users users = Mockito.mock(Users.class);
    final Address address = Mockito.mock(Address.class);

    final var result = mapper.toDomain(users, address);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertSame(users, result.getUsers());
    Assertions.assertSame(address, result.getAddress());
    Mockito.verifyNoInteractions(usersMapper);
  }

  @Test
  @DisplayName("Deve retornar null quando model for null")
  void shouldReturnNullWhenModelIsNull() {
    final var result = mapper.toOutput(null);

    Assertions.assertNull(result);
    Mockito.verifyNoInteractions(usersMapper);
  }

  @Test
  @DisplayName("Deve mapear domain para output quando users estiver presente")
  void shouldMapDomainToOutputWhenUsersIsPresent() {
    final var uuid = UUID.randomUUID();

    final Address address = Mockito.mock(Address.class);
    Mockito.when(address.getStreet()).thenReturn("Rua A");
    Mockito.when(address.getNumber()).thenReturn("123");
    Mockito.when(address.getComplement()).thenReturn("Apto 10");
    Mockito.when(address.getNeighborhood()).thenReturn("Centro");
    Mockito.when(address.getCity()).thenReturn("São Paulo");
    Mockito.when(address.getState()).thenReturn("SP");
    Mockito.when(address.getCountry()).thenReturn("Brasil");
    Mockito.when(address.getZipCode()).thenReturn("01000-000");

    final Users users = Mockito.mock(Users.class);

    final UsersAddress model = Mockito.mock(UsersAddress.class);
    Mockito.when(model.getUuid()).thenReturn(uuid);
    Mockito.when(model.getAddress()).thenReturn(address);
    Mockito.when(model.getUsers()).thenReturn(users);

    final UsersOutput usersOutput = Mockito.mock(UsersOutput.class);
    Mockito.when(usersMapper.toOutput(users)).thenReturn(usersOutput);

    final UsersAddressOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Rua A", result.getStreet());
    Assertions.assertEquals("123", result.getNumber());
    Assertions.assertEquals("Apto 10", result.getComplement());
    Assertions.assertEquals("Centro", result.getNeighborhood());
    Assertions.assertEquals("São Paulo", result.getCity());
    Assertions.assertEquals("SP", result.getState());
    Assertions.assertEquals("Brasil", result.getCountry());
    Assertions.assertEquals("01000-000", result.getZipCode());
    Assertions.assertSame(usersOutput, result.getUsers());

    Mockito.verify(usersMapper, Mockito.times(1)).toOutput(users);
  }

  @Test
  @DisplayName("Deve mapear domain para output quando users for null")
  void shouldMapDomainToOutputWhenUsersIsNull() {
    final var uuid = UUID.randomUUID();

    final Address address = Mockito.mock(Address.class);
    Mockito.when(address.getStreet()).thenReturn("Rua B");
    Mockito.when(address.getNumber()).thenReturn("456");
    Mockito.when(address.getComplement()).thenReturn(null);
    Mockito.when(address.getNeighborhood()).thenReturn("Bairro X");
    Mockito.when(address.getCity()).thenReturn("Rio de Janeiro");
    Mockito.when(address.getState()).thenReturn("RJ");
    Mockito.when(address.getCountry()).thenReturn("Brasil");
    Mockito.when(address.getZipCode()).thenReturn("20000-000");

    final UsersAddress model = Mockito.mock(UsersAddress.class);
    Mockito.when(model.getUuid()).thenReturn(uuid);
    Mockito.when(model.getAddress()).thenReturn(address);
    Mockito.when(model.getUsers()).thenReturn(null);

    final UsersAddressOutput result = mapper.toOutput(model);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("Rua B", result.getStreet());
    Assertions.assertEquals("456", result.getNumber());
    Assertions.assertNull(result.getComplement());
    Assertions.assertEquals("Bairro X", result.getNeighborhood());
    Assertions.assertEquals("Rio de Janeiro", result.getCity());
    Assertions.assertEquals("RJ", result.getState());
    Assertions.assertEquals("Brasil", result.getCountry());
    Assertions.assertEquals("20000-000", result.getZipCode());
    Assertions.assertNull(result.getUsers());

    Mockito.verifyNoInteractions(usersMapper);
  }
}
