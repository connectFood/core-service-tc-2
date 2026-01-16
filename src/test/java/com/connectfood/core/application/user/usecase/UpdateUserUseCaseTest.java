package com.connectfood.core.application.user.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.user.dto.UserInput;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

  @Mock
  private UserGateway repository;

  @Mock
  private UserAppMapper mapper;

  @Mock
  private UserTypeGateway userTypeGateway;

  @InjectMocks
  private UpdateUserUseCase useCase;

  @Test
  @DisplayName("Deve atualizar usuário mantendo o tipo quando usersTypeUuid for o mesmo")
  void shouldUpdateKeepingUsersTypeWhenUuidIsTheSame() {
    final var userUuid = UUID.randomUUID();
    final var usersTypeUuid = UUID.randomUUID();

    final UserInput input = Mockito.mock(UserInput.class);
    Mockito.when(input.getUsersTypeUuid())
        .thenReturn(usersTypeUuid);

    final UserType currentType = Mockito.mock(UserType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(usersTypeUuid);

    final User existingUser = Mockito.mock(User.class);
    Mockito.when(existingUser.getUserType())
        .thenReturn(currentType);
    Mockito.when(existingUser.getPasswordHash())
        .thenReturn("hashAntigo");

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.of(existingUser));

    final User domainToUpdate = Mockito.mock(User.class);
    Mockito.when(mapper.toDomain(userUuid, input, "hashAntigo", currentType))
        .thenReturn(domainToUpdate);

    final User updatedUser = Mockito.mock(User.class);
    Mockito.when(repository.update(userUuid, domainToUpdate))
        .thenReturn(updatedUser);

    final UserOutput expectedOutput = Mockito.mock(UserOutput.class);
    Mockito.when(mapper.toOutput(updatedUser))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(userUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(userUuid);

    Mockito.verify(existingUser, Mockito.times(2))
        .getUserType();
    Mockito.verify(existingUser, Mockito.times(1))
        .getPasswordHash();

    Mockito.verifyNoInteractions(userTypeGateway);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(userUuid, input, "hashAntigo", currentType);

    Mockito.verify(repository, Mockito.times(1))
        .update(userUuid, domainToUpdate);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedUser);

    Mockito.verifyNoMoreInteractions(repository, mapper, existingUser);
  }

  @Test
  @DisplayName("Deve atualizar usuário buscando novo tipo quando usersTypeUuid for diferente")
  void shouldUpdateFetchingNewUsersTypeWhenUuidIsDifferent() {
    final var userUuid = UUID.randomUUID();
    final var currentTypeUuid = UUID.randomUUID();
    final var newTypeUuid = UUID.randomUUID();

    final UserInput input = Mockito.mock(UserInput.class);
    Mockito.when(input.getUsersTypeUuid())
        .thenReturn(newTypeUuid);

    final UserType currentType = Mockito.mock(UserType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final User existingUser = Mockito.mock(User.class);
    Mockito.when(existingUser.getUserType())
        .thenReturn(currentType);
    Mockito.when(existingUser.getPasswordHash())
        .thenReturn("hashAntigo");

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.of(existingUser));

    final UserType newType = Mockito.mock(UserType.class);
    Mockito.when(userTypeGateway.findByUuid(newTypeUuid))
        .thenReturn(Optional.of(newType));

    final User domainToUpdate = Mockito.mock(User.class);
    Mockito.when(mapper.toDomain(userUuid, input, "hashAntigo", newType))
        .thenReturn(domainToUpdate);

    final User updatedUser = Mockito.mock(User.class);
    Mockito.when(repository.update(userUuid, domainToUpdate))
        .thenReturn(updatedUser);

    final UserOutput expectedOutput = Mockito.mock(UserOutput.class);
    Mockito.when(mapper.toOutput(updatedUser))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(userUuid, input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(userUuid);

    Mockito.verify(existingUser, Mockito.times(2))
        .getUserType();
    Mockito.verify(existingUser, Mockito.times(1))
        .getPasswordHash();

    Mockito.verify(userTypeGateway, Mockito.times(1))
        .findByUuid(newTypeUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(userUuid, input, "hashAntigo", newType);

    Mockito.verify(repository, Mockito.times(1))
        .update(userUuid, domainToUpdate);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedUser);

    Mockito.verifyNoMoreInteractions(repository, mapper, userTypeGateway, existingUser);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando usuário não existir")
  void shouldThrowNotFoundWhenUserDoesNotExist() {
    final var userUuid = UUID.randomUUID();

    final UserInput input = Mockito.mock(UserInput.class);

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(userUuid, input)
    );

    Assertions.assertEquals("User not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(userUuid);

    Mockito.verifyNoInteractions(mapper, userTypeGateway);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando tipo informado não existir e uuid for diferente")
  void shouldThrowNotFoundWhenUsersTypeDoesNotExistAndUuidIsDifferent() {
    final var userUuid = UUID.randomUUID();
    final var currentTypeUuid = UUID.randomUUID();
    final var newTypeUuid = UUID.randomUUID();

    final UserInput input = Mockito.mock(UserInput.class);
    Mockito.when(input.getUsersTypeUuid())
        .thenReturn(newTypeUuid);

    final UserType currentType = Mockito.mock(UserType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final User existingUser = Mockito.mock(User.class);
    Mockito.when(existingUser.getUserType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.of(existingUser));

    Mockito.when(userTypeGateway.findByUuid(newTypeUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(userUuid, input)
    );

    Assertions.assertEquals("User type not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(existingUser, Mockito.times(2))
        .getUserType();

    Mockito.verify(userTypeGateway, Mockito.times(1))
        .findByUuid(newTypeUuid);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());

    Mockito.verifyNoMoreInteractions(repository, userTypeGateway, existingUser);
  }
}
