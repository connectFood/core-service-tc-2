package com.connectfood.core.application.usertype.usecase;

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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserTypeUseCaseTest {

  @Mock
  private UsersTypeRepository repository;

  @Mock
  private UsersTypeAppMapper mapper;

  @InjectMocks
  private FindUserTypeUseCase useCase;

  @Test
  @DisplayName("Deve retornar tipo de usuário com sucesso quando existir")
  void shouldFindUserTypeSuccessfully() {
    // Arrange
    final var uuid = UUID.randomUUID();
    final var usersType = new UsersType(uuid, "ADMIN", "Administrador");
    final var output = new UsersTypeOutput(uuid, "ADMIN", "Administrador");

    // Mocks
    when(repository.findByUuid(uuid)).thenReturn(Optional.of(usersType));
    when(mapper.toOutput(usersType)).thenReturn(output);

    // Act
    final var result = useCase.execute(uuid);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals(output.getName(), result.getName()); // Se for Record, use .name()

    verify(repository, times(1)).findByUuid(uuid);
    verify(mapper, times(1)).toOutput(usersType);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando id não existir")
  void shouldThrowNotFoundWhenIdDoesNotExist() {
    // Arrange
    final var uuid = UUID.randomUUID();
    // Simula que não encontrou nada
    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    // Act & Assert
    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid);
    });

    // Garante que o mapper nunca foi chamado
    verify(mapper, never()).toOutput(any());
  }
}
