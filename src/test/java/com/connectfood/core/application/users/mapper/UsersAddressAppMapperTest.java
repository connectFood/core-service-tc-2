package com.connectfood.core.application.users.mapper;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.User;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersAddressAppMapperTest {

  private final UsersAddressAppMapper mapper = new UsersAddressAppMapper();

  @Test
  @DisplayName("Deve retornar null quando users for null")
  void shouldReturnNullWhenUsersIsNull() {
    final Address address = Mockito.mock(Address.class);

    final var result = mapper.toDomain(null, address);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve retornar null quando address for null")
  void shouldReturnNullWhenAddressIsNull() {
    final User user = Mockito.mock(User.class);

    final var result = mapper.toDomain(user, null);

    Assertions.assertNull(result);
  }

  @Test
  @DisplayName("Deve criar UsersAddress quando users e address forem válidos")
  void shouldCreateUsersAddressWhenValidData() {
    final User user = Mockito.mock(User.class);
    final Address address = Mockito.mock(Address.class);

    final var result = mapper.toDomain(user, address);

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertSame(user, result.getUser());
    Assertions.assertSame(address, result.getAddress());
  }
}
