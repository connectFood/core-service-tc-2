package com.connectfood.core.application.usertype.usecase;

import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UserTypeGateway;
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
  private UserTypeGateway repository;

  @Mock
  private UsersTypeAppMapper mapper;

  @InjectMocks
  private FindUserTypeUseCase useCase;

  @Test
  @DisplayName("Deve retornar tipo de usuário com sucesso quando existir")
  void shouldFindUserTypeSuccessfully() {
    final var uuid = UUID.randomUUID();
    final var usersType = new UserType(uuid, "ADMIN", "Administrador");
    final var output = new UsersTypeOutput(uuid, "ADMIN", "Administrador");

    when(repository.findByUuid(uuid)).thenReturn(Optional.of(usersType));
    when(mapper.toOutput(usersType)).thenReturn(output);

    final var result = useCase.execute(uuid);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(output.getUuid(), result.getUuid());
    Assertions.assertEquals(output.getName(), result.getName());

    verify(repository, times(1)).findByUuid(uuid);
    verify(mapper, times(1)).toOutput(usersType);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando id não existir")
  void shouldThrowNotFoundWhenIdDoesNotExist() {
    final var uuid = UUID.randomUUID();
    when(repository.findByUuid(uuid)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(uuid);
    });

    verify(mapper, never()).toOutput(any());
  }
}
