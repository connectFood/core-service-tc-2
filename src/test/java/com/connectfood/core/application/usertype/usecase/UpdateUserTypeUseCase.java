package com.connectfood.core.application.usertype.usecase;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UsersTypeGateway;
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
  private UsersTypeGateway repository;

  @Mock
  private UsersTypeAppMapper mapper;

  @InjectMocks
  private UpdateUserTypeUseCase useCase;

  @Test
  @DisplayName("Deve atualizar tipo de usuário com sucesso quando existir")
  void shouldUpdateUserTypeSuccessfully() {
    final var uuid = UUID.randomUUID();
    final var input = new UsersTypeInput("ADMIN_UPDATE", "Administrador Atualizado");

    final var existingUserType = new UserType(uuid, "ADMIN", "Admin Antigo");

    final var updatedUserType = new UserType(uuid, "ADMIN_UPDATE", "Administrador Atualizado");

    final var output = new UsersTypeOutput(uuid, "ADMIN_UPDATE", "Administrador Atualizado");

    when(repository.findByUuid(uuid)).thenReturn(Optional.of(existingUserType));

    when(mapper.toDomain(uuid, input)).thenReturn(updatedUserType);

    when(repository.update(uuid, updatedUserType)).thenReturn(updatedUserType);

    when(mapper.toOutput(updatedUserType)).thenReturn(output);

    final var result = useCase.execute(uuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals("ADMIN_UPDATE", result.getName());

    verify(repository, times(1)).findByUuid(uuid);
    verify(repository, times(1)).update(uuid, updatedUserType);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException ao tentar atualizar registro inexistente")
  void shouldThrowNotFoundWhenUserTypeDoesNotExist() {
    final var uuid = UUID.randomUUID();
    final var input = new UsersTypeInput("Nome", "Desc");

    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid, input);
    });

    verify(repository, never()).update(any(), any());
    verify(mapper, never()).toDomain(any(), any());
  }
}
