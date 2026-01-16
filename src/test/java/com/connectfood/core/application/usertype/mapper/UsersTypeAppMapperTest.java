package com.connectfood.core.application.usertype.mapper;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.domain.model.UserType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UsersTypeAppMapperTest {

  @InjectMocks
  private UsersTypeAppMapper mapper;

  @Test
  @DisplayName("Deve converter Input e UUID para Domínio corretamente")
  void shouldMapInputAndUuidToDomain() {
    final var uuid = UUID.randomUUID();
    final var input = new UsersTypeInput("ADMIN", "Administrador do sistema");

    final var result = mapper.toDomain(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ADMIN", result.getName());
  }

  @Test
  @DisplayName("Deve retornar null no toDomain(UUID) se input for nulo")
  void shouldReturnNullWhenInputWithUuidIsNull() {
    final var uuid = UUID.randomUUID();
    Assertions.assertNull(mapper.toDomain(uuid, null));
  }


  @Test
  @DisplayName("Deve converter Input para Domínio corretamente (UUID gerado ou nulo)")
  void shouldMapInputToDomain() {
    final var input = new UsersTypeInput("CLIENT", "Cliente Padrão");

    final var result = mapper.toDomain(input);

    Assertions.assertNotNull(result);
    Assertions.assertEquals("CLIENT", result.getName());
  }

  @Test
  @DisplayName("Deve retornar null no toDomain simples se input for nulo")
  void shouldReturnNullWhenInputIsNull() {
    Assertions.assertNull(mapper.toDomain(null));
  }


  @Test
  @DisplayName("Deve converter Domínio para Output corretamente")
  void shouldMapDomainToOutput() {
    final var uuid = UUID.randomUUID();
    final var domain = new UserType(uuid, "TEST", "Teste");

    final var result = mapper.toOutput(domain);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("TEST", result.getName());
  }

  @Test
  @DisplayName("Deve retornar null no toOutput se domínio for nulo")
  void shouldReturnNullWhenDomainIsNull() {
    Assertions.assertNull(mapper.toOutput(null));
  }
}
