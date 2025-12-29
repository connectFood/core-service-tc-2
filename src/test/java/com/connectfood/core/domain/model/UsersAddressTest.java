package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

class UsersAddressTest {

  @Test
  @DisplayName("Deve criar um vínculo de endereço de usuário com UUID explícito e dados válidos")
  void shouldCreateUsersAddressWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final Users users = Mockito.mock(Users.class);
    final Address address = Mockito.mock(Address.class);

    final var usersAddress = new UsersAddress(uuid, users, address);

    Assertions.assertEquals(uuid, usersAddress.getUuid());
    Assertions.assertEquals(users, usersAddress.getUsers());
    Assertions.assertEquals(address, usersAddress.getAddress());
  }

  @Test
  @DisplayName("Deve criar um vínculo de endereço de usuário sem UUID explícito e dados válidos")
  void shouldCreateUsersAddressWithoutExplicitUuid() {
    final Users users = Mockito.mock(Users.class);
    final Address address = Mockito.mock(Address.class);

    final var usersAddress = new UsersAddress(users, address);

    Assertions.assertNotNull(usersAddress.getUuid());
    Assertions.assertEquals(users, usersAddress.getUsers());
    Assertions.assertEquals(address, usersAddress.getAddress());
  }

  @Test
  @DisplayName("Não deve criar um vínculo de endereço de usuário sem users e lançar BadRequestException")
  void shouldNotCreateUsersAddressWithoutUsersAndThrowBadRequestException() {
    final Address address = Mockito.mock(Address.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new UsersAddress(UUID.randomUUID(), null, address)
    );

    Assertions.assertEquals("Users is required", exception.getMessage());
  }

  @Test
  @DisplayName("Não deve criar um vínculo de endereço de usuário sem address e lançar BadRequestException")
  void shouldNotCreateUsersAddressWithoutAddressAndThrowBadRequestException() {
    final Users users = Mockito.mock(Users.class);

    final var exception = Assertions.assertThrows(
        BadRequestException.class,
        () -> new UsersAddress(UUID.randomUUID(), users, null)
    );

    Assertions.assertEquals("Address is required", exception.getMessage());
  }
}
