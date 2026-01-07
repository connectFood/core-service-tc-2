package com.connectfood.core.application.users.usecase;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.repository.UsersTypeRepository;
import com.connectfood.core.domain.utils.PasswordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUsersUseCaseTest {

  @Mock
  private UsersRepository repository;
  @Mock
  private UsersAppMapper mapper;
  @Mock
  private UsersTypeRepository usersTypeRepository;
  @Mock
  private PasswordUtils passwordUtils;

  @InjectMocks
  private CreateUsersUseCase useCase;

  @Test
  @DisplayName("Deve criar usuário com sucesso quando o tipo de usuário existe")
  void shouldCreateUserSuccessfully() {
    // ARRANGE
    final var uuidUserType = UUID.randomUUID();
    final var uuidUser = UUID.randomUUID();

    final var input = new UsersInput("Pilar", "pilar@email.com", "senha123", uuidUserType);

    final var usersType = new UsersType(uuidUserType, "CLIENT", "Cliente");

    // Objeto de domínio esperado após conversão
    final var userDomain = new Users(uuidUser, "Pilar", "pilar@email.com", "hashSenha", usersType);

    // Output esperado
    final var output = new UsersOutput(uuidUser, "Pilar", "pilar@email.com", new UsersTypeOutput(uuidUserType, "CLIENT", "Cliente"));

    // Mocks
    when(passwordUtils.encode("senha123")).thenReturn("hashSenha");
    when(usersTypeRepository.findByUuid(uuidUserType)).thenReturn(Optional.of(usersType));

    // O mapper.toDomain recebe (input, senhaCriptografada, usersType)
    when(mapper.toDomain(input, "hashSenha", usersType)).thenReturn(userDomain);

    when(repository.save(userDomain)).thenReturn(userDomain);
    when(mapper.toOutput(userDomain)).thenReturn(output);

    // ACT
    final var result = useCase.execute(input);

    // ASSERT
    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals("Pilar", result.getFullName()); // Validando com getFullName() conforme seu DTO

    // Verificações de segurança
    verify(passwordUtils, times(1)).encode("senha123"); // Garante que criptografou
    verify(repository, times(1)).save(userDomain);      // Garante que salvou
  }

  @Test
  @DisplayName("Deve lançar erro NotFoundException se o tipo de usuário não existir")
  void shouldThrowNotFoundWhenUserTypeDoesNotExist() {
    // ARRANGE
    final var uuidUserType = UUID.randomUUID();
    final var input = new UsersInput("Pilar", "pilar@email.com", "senha123", uuidUserType);

    when(passwordUtils.encode(any())).thenReturn("hashQualquer");
    // Simulamos que o banco NÃO achou o tipo de usuário
    when(usersTypeRepository.findByUuid(uuidUserType)).thenReturn(Optional.empty());

    // ACT & ASSERT
    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(input);
    });

    // Garante que NUNCA tentou salvar o usuário se deu erro antes
    verify(repository, never()).save(any());
  }
}
