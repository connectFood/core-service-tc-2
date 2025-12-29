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
class UpdateUsersUseCaseTest {

  @Mock private UsersRepository repository;
  @Mock private UsersAppMapper mapper;
  @Mock private UsersTypeRepository usersTypeRepository;
  @Mock private PasswordUtils passwordUtils; // Caso sua lógica atualize senha

  @InjectMocks
  private UpdateUsersUseCase useCase;

  @Test
  @DisplayName("Deve atualizar usuário com sucesso")
  void shouldUpdateUserSuccessfully() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var uuidUserType = UUID.randomUUID();

    final var input = new UsersInput("Nome Novo", "novo@email.com", "123", uuidUserType);
    final var existingUser = new Users(uuid, "Nome Antigo", "antigo@email.com", "hashAntigo", new UsersType("CLIENT", "C"));
    final var updatedUser = new Users(uuid, "Nome Novo", "novo@email.com", "hashNovo", new UsersType(uuidUserType, "ADMIN", "A"));

    // Mocks
    when(repository.findByUuid(uuid)).thenReturn(Optional.of(existingUser));
    when(usersTypeRepository.findByUuid(uuidUserType)).thenReturn(Optional.of(new UsersType(uuidUserType, "ADMIN", "A")));

    // Simula o mapper convertendo o input para o domínio atualizado
    when(mapper.toDomain(eq(input), any(), any())).thenReturn(updatedUser);
    when(repository.save(updatedUser)).thenReturn(updatedUser);

    // Output esperado
    final var output = new UsersOutput(uuid, "Nome Novo", "novo@email.com", null);
    when(mapper.toOutput(updatedUser)).thenReturn(output);

    // Act
    final var result = useCase.execute(uuid, input);

    // Assert
    Assertions.assertEquals("Nome Novo", result.getFullName());
    verify(repository, times(1)).save(updatedUser);
  }

  @Test
  @DisplayName("Deve lançar exceção se tentar atualizar usuário que não existe")
  void shouldThrowNotFoundWhenUserDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final var input = new UsersInput("Nome", "email", "senha", UUID.randomUUID());

    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid, input);
    });

    verify(repository, never()).save(any());
  }
}
