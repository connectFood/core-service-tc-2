package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersTest {

  @Test
  @DisplayName("Deve criar um usuário com dados válidos")
  void shouldCreateUserWithValidData() {
    final var uuid = UUID.randomUUID();
    final var userType = new UsersType(UUID.randomUUID(), "CLIENT", "Cliente");

    final var user = new Users(uuid, "Pilar Calderon", "pilarcalderon@gmail.com", "hashSenha123", userType);

    Assertions.assertNotNull(user);
    Assertions.assertEquals(uuid, user.getUuid());
    Assertions.assertEquals("Pilar Calderon", user.getFullName());
  }

  @Test
  @DisplayName("Deve lançar exceção quando criar usuário sem e-mail")
  void shouldThrowExceptionWhenEmailIsNull() {
    final var userType = new UsersType("CLIENT", "Cliente");

    Assertions.assertThrows(BadRequestException.class,
        () -> new Users(UUID.randomUUID(), "Pilar", null, "senha", userType)
    );
  }

  @Test
  @DisplayName("Deve gerar UUID automaticamente se for nulo")
  void shouldGenerateUuidAutomatically() {
    final var userType = new UsersType("CLIENT", "Cliente");

    final var user = new Users(null, "Pilar", "pilarcalderon@gmail.com", "senha", userType);

    Assertions.assertNotNull(user.getUuid());
  }
}
