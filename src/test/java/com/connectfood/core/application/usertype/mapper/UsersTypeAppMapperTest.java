package com.connectfood.core.application.usertype.mapper;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.domain.model.UsersType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UsersTypeAppMapperTest {

  private final UsersTypeAppMapper mapper = new UsersTypeAppMapper(); // Instância real do mapper

  @Test
  @DisplayName("Deve converter Input para Domínio corretamente")
  void shouldMapInputToDomain() {
    final var input = new UsersTypeInput("TEST", "Desc");

    final var result = mapper.toDomain(input);

    Assertions.assertEquals("TEST", result.getName());
    Assertions.assertEquals("Desc", result.getDescription());
    Assertions.assertNotNull(result.getUuid()); // O construtor do domínio gera UUID
  }

  @Test
  @DisplayName("Deve converter Domínio para Output corretamente")
  void shouldMapDomainToOutput() {
    final var domain = new UsersType(UUID.randomUUID(), "TEST", "Desc");

    final var result = mapper.toOutput(domain);

    Assertions.assertEquals(domain.getUuid(), result.getUuid());
    Assertions.assertEquals("TEST", result.getName());
    Assertions.assertEquals("Desc", result.getDescription());
  }
}
