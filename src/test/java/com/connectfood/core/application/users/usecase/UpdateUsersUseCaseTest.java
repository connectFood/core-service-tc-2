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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUsersUseCaseTest {

  @Mock private UsersRepository repository;
  @Mock private UsersAppMapper mapper;
  @Mock private UsersTypeRepository usersTypeRepository;

  @InjectMocks
  private UpdateUsersUseCase useCase;

  @Test
  @DisplayName("Deve atualizar usuário com sucesso")
  void shouldUpdateUserSuccessfully() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var uuidUserType = UUID.randomUUID();

    final var input = new UsersInput("Nome Novo", "novo@email.com", "123", uuidUserType);

    // Dados antigos (que virão do banco)
    final var existingUser = new Users(uuid, "Nome Antigo", "antigo@email.com", "hashAntigo", new UsersType(uuidUserType, "ADMIN", "A"));

    // Dados novos (objeto atualizado)
    final var updatedUser = new Users(uuid, "Nome Novo", "novo@email.com", "hashAntigo", new UsersType(uuidUserType, "ADMIN", "A"));

    final var output = new UsersOutput(uuid, "Nome Novo", "novo@email.com", new UsersTypeOutput(uuidUserType, "ADMIN", "A"));

    // Mocks
    when(repository.findByUuid(uuid)).thenReturn(Optional.of(existingUser));
    when(mapper.toDomain(eq(uuid), eq(input), any(), any())).thenReturn(updatedUser);
    when(repository.update(uuid, updatedUser)).thenReturn(updatedUser);
    when(mapper.toOutput(updatedUser)).thenReturn(output);

    // Act
    final var result = useCase.execute(uuid, input);

    // Assert
    Assertions.assertEquals("Nome Novo", result.getFullName()); // Lembre do getFullName()
    verify(repository, times(1)).update(uuid, updatedUser);
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

    verify(repository, never()).update(any(), any());
  }
}
