package com.connectfood.core.application.usertype.usecase;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.UsersType;
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
class UpdateUserTypeUseCaseTest {

  @Mock
  private UsersTypeRepository repository;

  @Mock
  private UsersTypeAppMapper mapper;

  @InjectMocks
  private UpdateUserTypeUseCase useCase;

  @Test
  @DisplayName("Deve atualizar tipo de usuário com sucesso quando existir")
  void shouldUpdateUserTypeSuccessfully() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var input = new UsersTypeInput("ADMIN_UPDATE", "Administrador Atualizado");

    // Dados antigos (simulando que existe no banco)
    final var existingUserType = new UsersType(uuid, "ADMIN", "Admin Antigo");

    // Dados novos (objeto de domínio atualizado)
    final var updatedUserType = new UsersType(uuid, "ADMIN_UPDATE", "Administrador Atualizado");

    // Output esperado
    final var output = new UsersTypeOutput(uuid, "ADMIN_UPDATE", "Administrador Atualizado");

    // Mocks
    // 1. Verifica se existe
    when(repository.findByUuid(uuid)).thenReturn(Optional.of(existingUserType));

    // 2. Converte Input -> Domain
    when(mapper.toDomain(uuid, input)).thenReturn(updatedUserType);

    // 3. Atualiza no Banco
    when(repository.update(uuid, updatedUserType)).thenReturn(updatedUserType);

    // 4. Converte Domain -> Output
    when(mapper.toOutput(updatedUserType)).thenReturn(output);

    // Act
    final var result = useCase.execute(uuid, input);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals("ADMIN_UPDATE", result.getName()); // Use .name() se for Record

    verify(repository, times(1)).findByUuid(uuid);
    verify(repository, times(1)).update(uuid, updatedUserType);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException ao tentar atualizar registro inexistente")
  void shouldThrowNotFoundWhenUserTypeDoesNotExist() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var input = new UsersTypeInput("Nome", "Desc");

    // Simula que não encontrou nada
    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    // Act & Assert
    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid, input);
    });

    // Garante que NUNCA chamou o update
    verify(repository, never()).update(any(), any());
    verify(mapper, never()).toDomain(any(), any());
  }
}
