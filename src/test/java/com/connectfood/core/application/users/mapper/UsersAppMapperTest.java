package com.connectfood.core.application.users.mapper;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersAppMapperTest {

  @Mock
  private UsersTypeAppMapper usersTypeMapper;

  @Mock
  private AddressAppMapper addressMapper;

  @InjectMocks
  private UsersAppMapper mapper;

  // --- Métodos Auxiliares para criar objetos válidos ---
  // (Isso evita erros de validação "Name too short" ou "Required")
  private UsersType createValidUsersType() {
    return new UsersType(UUID.randomUUID(), "CLIENT", "Cliente Padrão");
  }

  private Users createValidUser() {
    return new Users(UUID.randomUUID(), "Maria Pilar", "pilar@test.com", "hashSenha123", createValidUsersType());
  }

  // --- Testes do toDomain (Criação) ---

  @Test
  @DisplayName("Deve converter Input para Domínio corretamente (sem UUID)")
  void shouldMapInputToDomain() {
    // Arrange
    final var usersType = createValidUsersType();
    final var input = new UsersInput("Maria Pilar", "pilar@test.com", "senha123", usersType.getUuid());
    final var passwordHash = "hashSeguro";

    // Act
    final var result = mapper.toDomain(input, passwordHash, usersType);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.getUuid());
    Assertions.assertEquals(input.getFullName(), result.getFullName());
    Assertions.assertEquals(input.getEmail(), result.getEmail());
    Assertions.assertEquals(passwordHash, result.getPasswordHash());
    Assertions.assertEquals(usersType, result.getUsersType());
  }

  @Test
  @DisplayName("Deve retornar null no toDomain se input for nulo")
  void shouldReturnNullWhenInputIsNull() {

    final var result = mapper.toDomain(null, "hash", createValidUsersType());
    Assertions.assertNull(result);
  }

  // --- Testes do toDomain (Atualização - com UUID) ---

  @Test
  @DisplayName("Deve converter Input e UUID para Domínio corretamente")
  void shouldMapInputAndUuidToDomain() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var usersType = createValidUsersType();
    final var input = new UsersInput("Pilar Atualizada", "novo@test.com", "senhaNova", usersType.getUuid());
    final var passwordHash = "hashNovo";

    // Act
    final var result = mapper.toDomain(uuid, input, passwordHash, usersType);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(uuid, result.getUuid());
    Assertions.assertEquals(input.getFullName(), result.getFullName());
    Assertions.assertEquals(input.getEmail(), result.getEmail());
    Assertions.assertEquals(passwordHash, result.getPasswordHash());
    Assertions.assertEquals(usersType, result.getUsersType());
  }

  @Test
  @DisplayName("Deve retornar null no toDomain com UUID se input for nulo")
  void shouldReturnNullWhenInputWithUuidIsNull() {
    final var result = mapper.toDomain(UUID.randomUUID(), null, "hash", null);
    Assertions.assertNull(result);
  }

  // --- Testes do toOutput (Simples) ---

  @Test
  @DisplayName("Deve converter Domínio para Output corretamente (sem Endereço)")
  void shouldMapDomainToOutput() {
    // Arrange
    final var user = createValidUser();
    final var usersTypeOutput = new UsersTypeOutput(user.getUsersType().getUuid(), "CLIENT", "Cliente Padrão");

    when(usersTypeMapper.toOutput(any(UsersType.class))).thenReturn(usersTypeOutput);

    // Act
    final var result = mapper.toOutput(user);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(user.getUuid(), result.getUuid());
    Assertions.assertEquals(user.getFullName(), result.getFullName());
    Assertions.assertEquals(user.getEmail(), result.getEmail());
    Assertions.assertEquals(usersTypeOutput, result.getUsersType());
    Assertions.assertNull(result.getAddress());

    verify(usersTypeMapper, times(1)).toOutput(any(UsersType.class));
  }

  @Test
  @DisplayName("Deve retornar null no toOutput se modelo for nulo")
  void shouldReturnNullWhenModelIsNull() {
    Assertions.assertNull(mapper.toOutput(null));
  }

  // --- Testes do toOutput (Com Endereço) ---

  @Test
  @DisplayName("Deve converter Domínio e Endereço para Output corretamente")
  void shouldMapDomainAndAddressToOutput() {
    // Arrange
    final var user = createValidUser();
    final var address = new Address(UUID.randomUUID(), "Rua A", "10", "Apto 1", "Bairro", "Cidade", "SP", "CEP", "BR");

    final var usersTypeOutput = new UsersTypeOutput(user.getUsersType().getUuid(), "CLIENT", "Cliente Padrão");
    final var addressOutput = new AddressOutput(address.getUuid(), "Rua A", "10", "Apto 1", "Bairro", "Cidade", "SP", "BR", "CEP");

    when(usersTypeMapper.toOutput(any(UsersType.class))).thenReturn(usersTypeOutput);
    when(addressMapper.toOutput(address)).thenReturn(addressOutput);

    // Act
    final var result = mapper.toOutput(user, address);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(user.getUuid(), result.getUuid());
    Assertions.assertEquals(usersTypeOutput, result.getUsersType());
    Assertions.assertEquals(addressOutput, result.getAddress());

    verify(addressMapper, times(1)).toOutput(address);
  }

  @Test
  @DisplayName("Deve retornar null no toOutput com Address se algum parâmetro for nulo")
  void shouldReturnNullWhenModelOrAddressIsNull() {

    final var user = createValidUser();


    final var address = new Address(UUID.randomUUID(), "Rua", "1", "C", "B", "C", "S", "P", "Z");

    Assertions.assertNull(mapper.toOutput(null, address));
    Assertions.assertNull(mapper.toOutput(user, null));
  }
}
