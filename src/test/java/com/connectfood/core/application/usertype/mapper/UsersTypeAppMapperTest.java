package com.connectfood.core.application.usertype.mapper;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.domain.model.UsersType;
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

  // --- Testes do toDomain (UUID + Input) - O que estava faltando ---

  @Test
  @DisplayName("Deve converter Input e UUID para Domínio corretamente")
  void shouldMapInputAndUuidToDomain() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var input = new UsersTypeInput("ADMIN", "Administrador do sistema");

    // Act
    final var result = mapper.toDomain(uuid, input);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("ADMIN", result.getName());
    // Se UsersTypeInput tiver descrição, descomente abaixo:
    // Assertions.assertEquals("Administrador do sistema", result.getDescription());
  }

  @Test
  @DisplayName("Deve retornar null no toDomain(UUID) se input for nulo")
  void shouldReturnNullWhenInputWithUuidIsNull() {
    final var uuid = UUID.randomUUID();
    // Isso cobre o "Missed Branch" se o código tiver um "if (input == null)"
    Assertions.assertNull(mapper.toDomain(uuid, null));
  }

  // --- Testes do toDomain (Só Input) ---

  @Test
  @DisplayName("Deve converter Input para Domínio corretamente (UUID gerado ou nulo)")
  void shouldMapInputToDomain() {
    // Arrange
    final var input = new UsersTypeInput("CLIENT", "Cliente Padrão");

    // Act
    final var result = mapper.toDomain(input);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals("CLIENT", result.getName());
    // Geralmente o UUID vem nulo ou é gerado depois, dependendo da sua lógica
  }

  @Test
  @DisplayName("Deve retornar null no toDomain simples se input for nulo")
  void shouldReturnNullWhenInputIsNull() {
    // Isso preenche a barra vermelha de cobertura do toDomain
    Assertions.assertNull(mapper.toDomain(null));
  }

  // --- Testes do toOutput ---

  @Test
  @DisplayName("Deve converter Domínio para Output corretamente")
  void shouldMapDomainToOutput() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var domain = new UsersType(uuid, "TEST", "Teste");

    // Act
    final var result = mapper.toOutput(domain);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals("TEST", result.getName()); // Use .name() se for Record
  }

  @Test
  @DisplayName("Deve retornar null no toOutput se domínio for nulo")
  void shouldReturnNullWhenDomainIsNull() {
    // Isso preenche a barra vermelha de cobertura do toOutput
    Assertions.assertNull(mapper.toOutput(null));
  }
}
