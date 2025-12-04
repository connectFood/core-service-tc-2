package com.connectfood.core.domain.model;

import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersTypeTest {

  @Test
  @DisplayName("Deve criar um tipo de usuário com UUID explícito e dados válidos")
  void shouldCreateUsersTypeWithExplicitUuid() {
    final var uuid = UUID.randomUUID();
    final var name = "TEST";
    final var description = "Test users type";

    final var userType = new UsersType(uuid, name, description);

    Assertions.assertEquals(uuid, userType.getUuid());
    Assertions.assertEquals(name, userType.getName());
    Assertions.assertEquals(description, userType.getDescription());
  }
}
