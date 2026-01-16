package com.connectfood.core.application.user.usecase;

import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UserGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveUserUseCaseTest {

  @Mock
  private UserGateway repository;

  @InjectMocks
  private RemoveUserUseCase useCase;

  @Test
  @DisplayName("Deve remover o usuário com sucesso se existir")
  void shouldDeleteUserSuccessfully() {
    final var uuid = UUID.randomUUID();
    final var user = new User(uuid, "Pilar", "email", "senha", new UserType(UUID.randomUUID(), "ADMIN", "Admin"));

    when(repository.findByUuid(uuid)).thenReturn(Optional.of(user));

    useCase.execute(uuid);

    verify(repository, times(1)).delete(uuid);
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar remover usuário inexistente")
  void shouldThrowExceptionWhenUserNotFound() {
    final var uuid = UUID.randomUUID();
    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid);
    });

    verify(repository, never()).delete(any());
  }
}
