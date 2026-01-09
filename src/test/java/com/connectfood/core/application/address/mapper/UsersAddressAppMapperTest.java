package com.connectfood.core.application.address.mapper;

import com.connectfood.core.application.users.mapper.UsersAddressAppMapper;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;

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
}
