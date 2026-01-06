package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

  @Test
  @DisplayName("Deve criar um tipo de usuário sem UUID explícito e dados válidos")
  void shouldCreateUsersTypeWithoutExplicitUuid() {
    final var name = "TEST";
    final var description = "Test users type";

    final var userType = new UsersType(name, description);

    Assertions.assertNotNull(userType.getUuid());
    Assertions.assertEquals(name, userType.getName());
    Assertions.assertEquals(description, userType.getDescription());
  }

  @Test
  @DisplayName("Não deve criar um tipo de usuário sem nome e lançar uma BadRequest")
  void shouldNotCreateUsersTypeWithoutNameAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var description = "Test users type";

    final var exception = Assertions.assertThrows(BadRequestException.class, () -> new UsersType(uuid, null,
            description
        )
    );

    Assertions.assertEquals("Name is required", exception.getMessage());

  }

  @Test
  @DisplayName("Não deve criar um tipo de usuário com nome em branco e lançar uma BadRequest")
  void shouldNotCreateUsersTypeWithNameIsBlankAndThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var description = "Test users type";

    final var exception = Assertions.assertThrows(BadRequestException.class, () -> new UsersType(uuid, "",
            description
        )
    );

    Assertions.assertEquals("Name is required", exception.getMessage());

  }

  @Test
  @DisplayName("Não deve criar un tipo de usuário com nome menor que 3 caracteres e deve lançar uma BadRequest")
  void shouldNotCreateUsersTypeWithNameShorterThan3CharactersAndShouldThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var name = "TE";
    final var description = "Test users type";

    final var exception = Assertions.assertThrows(BadRequestException.class, () -> new UsersType(uuid, name,
            description
        )
    );

    Assertions.assertEquals("Name must be between 3 and 255 characters", exception.getMessage());

  }

  @Test
  @DisplayName("Não deve criar um tipo de usuário com nome maior que 255 caracteres e deve lançar uma BadRequest")
  void shouldNotCreateUsersTypeWithNameLongerThan255CharactersAndShouldThrowBadRequest() {
    final var uuid = UUID.randomUUID();
    final var name = "Lorem ipsum dolor sit amet. Eos earum voluptatem non harum quibusdam in perferendis enim et "
        + "necessitatibus aliquam aut molestiae culpa. Est rerum consequatur in nulla iusto est debitis voluptas aut "
        + "autem provident qui voluptates laudantium aut autem incidu";
    final var description = "Test users type";

    final var exception = Assertions.assertThrows(BadRequestException.class, () -> new UsersType(uuid, name,
            description
        )
    );

    Assertions.assertEquals("Name must be between 3 and 255 characters", exception.getMessage());

  }
}
