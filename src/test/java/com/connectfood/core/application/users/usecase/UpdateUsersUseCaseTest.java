package com.connectfood.core.application.users.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UsersGateway;
import com.connectfood.core.domain.repository.UsersTypeGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUsersUseCaseTest {

  @Mock
  private UsersGateway repository;

  @Mock
  private UsersAppMapper mapper;

  @Mock
  private UsersTypeGateway usersTypeGateway;

  @InjectMocks
  private UpdateUsersUseCase useCase;

  @Test
  @DisplayName("Deve atualizar usuário mantendo o tipo quando usersTypeUuid for o mesmo")
  void shouldUpdateKeepingUsersTypeWhenUuidIsTheSame() {
    final var userUuid = UUID.randomUUID();
    final var usersTypeUuid = UUID.randomUUID();

    final UsersInput input = Mockito.mock(UsersInput.class);
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

    final UsersOutput expectedOutput = Mockito.mock(UsersOutput.class);
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

    Mockito.verifyNoInteractions(usersTypeGateway);

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

    final UsersInput input = Mockito.mock(UsersInput.class);
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
    Mockito.when(usersTypeGateway.findByUuid(newTypeUuid))
        .thenReturn(Optional.of(newType));

    final User domainToUpdate = Mockito.mock(User.class);
    Mockito.when(mapper.toDomain(userUuid, input, "hashAntigo", newType))
        .thenReturn(domainToUpdate);

    final User updatedUser = Mockito.mock(User.class);
    Mockito.when(repository.update(userUuid, domainToUpdate))
        .thenReturn(updatedUser);

    final UsersOutput expectedOutput = Mockito.mock(UsersOutput.class);
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

    Mockito.verify(usersTypeGateway, Mockito.times(1))
        .findByUuid(newTypeUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(userUuid, input, "hashAntigo", newType);

    Mockito.verify(repository, Mockito.times(1))
        .update(userUuid, domainToUpdate);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedUser);

    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeGateway, existingUser);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando usuário não existir")
  void shouldThrowNotFoundWhenUserDoesNotExist() {
    final var userUuid = UUID.randomUUID();

    final UsersInput input = Mockito.mock(UsersInput.class);

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(userUuid, input)
    );

    Assertions.assertEquals("User not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(userUuid);

    Mockito.verifyNoInteractions(mapper, usersTypeGateway);
    Mockito.verifyNoMoreInteractions(repository);
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando tipo informado não existir e uuid for diferente")
  void shouldThrowNotFoundWhenUsersTypeDoesNotExistAndUuidIsDifferent() {
    final var userUuid = UUID.randomUUID();
    final var currentTypeUuid = UUID.randomUUID();
    final var newTypeUuid = UUID.randomUUID();

    final UsersInput input = Mockito.mock(UsersInput.class);
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

    Mockito.when(usersTypeGateway.findByUuid(newTypeUuid))
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

    Mockito.verify(usersTypeGateway, Mockito.times(1))
        .findByUuid(newTypeUuid);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());

    Mockito.verifyNoMoreInteractions(repository, usersTypeGateway, existingUser);
  }
}
