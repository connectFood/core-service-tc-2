package com.connectfood.core.application.usertype.usecase;

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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveUserTypeUseCaseTest {

  @Mock
  private UsersTypeRepository repository;

  @InjectMocks
  private RemoveUserTypeUseCase useCase;

  @Test
  @DisplayName("Deve remover tipo de usuário com sucesso quando existir")
  void shouldRemoveUserTypeSuccessfully() {
    final var uuid = UUID.randomUUID();
    final var usersType = new UsersType(uuid, "CLIENT", "Cliente");

    when(repository.findByUuid(uuid)).thenReturn(Optional.of(usersType));

    useCase.execute(uuid);

    verify(repository, times(1)).findByUuid(uuid);
    verify(repository, times(1)).delete(uuid);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException ao tentar remover registo inexistente")
  void shouldThrowNotFoundWhenUserTypeDoesNotExist() {
    final var uuid = UUID.randomUUID();

    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid);
    });

    verify(repository, never()).delete(any());
  }
}
