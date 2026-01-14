package com.connectfood.core.application.users.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUsersUseCaseTest {

  @Mock
  private UsersRepository repository;

  @Mock
  private UsersAppMapper mapper;

  @Mock
  private UsersTypeRepository usersTypeRepository;

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

    final UsersType currentType = Mockito.mock(UsersType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(usersTypeUuid);

    final Users existingUser = Mockito.mock(Users.class);
    Mockito.when(existingUser.getUsersType())
        .thenReturn(currentType);
    Mockito.when(existingUser.getPasswordHash())
        .thenReturn("hashAntigo");

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.of(existingUser));

    final Users domainToUpdate = Mockito.mock(Users.class);
    Mockito.when(mapper.toDomain(userUuid, input, "hashAntigo", currentType))
        .thenReturn(domainToUpdate);

    final Users updatedUser = Mockito.mock(Users.class);
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
        .getUsersType();
    Mockito.verify(existingUser, Mockito.times(1))
        .getPasswordHash();

    Mockito.verifyNoInteractions(usersTypeRepository);

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

    final UsersType currentType = Mockito.mock(UsersType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final Users existingUser = Mockito.mock(Users.class);
    Mockito.when(existingUser.getUsersType())
        .thenReturn(currentType);
    Mockito.when(existingUser.getPasswordHash())
        .thenReturn("hashAntigo");

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.of(existingUser));

    final UsersType newType = Mockito.mock(UsersType.class);
    Mockito.when(usersTypeRepository.findByUuid(newTypeUuid))
        .thenReturn(Optional.of(newType));

    final Users domainToUpdate = Mockito.mock(Users.class);
    Mockito.when(mapper.toDomain(userUuid, input, "hashAntigo", newType))
        .thenReturn(domainToUpdate);

    final Users updatedUser = Mockito.mock(Users.class);
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
        .getUsersType();
    Mockito.verify(existingUser, Mockito.times(1))
        .getPasswordHash();

    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(newTypeUuid);

    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(userUuid, input, "hashAntigo", newType);

    Mockito.verify(repository, Mockito.times(1))
        .update(userUuid, domainToUpdate);

    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(updatedUser);

    Mockito.verifyNoMoreInteractions(repository, mapper, usersTypeRepository, existingUser);
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

    Mockito.verifyNoInteractions(mapper, usersTypeRepository);
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

    final UsersType currentType = Mockito.mock(UsersType.class);
    Mockito.when(currentType.getUuid())
        .thenReturn(currentTypeUuid);

    final Users existingUser = Mockito.mock(Users.class);
    Mockito.when(existingUser.getUsersType())
        .thenReturn(currentType);

    Mockito.when(repository.findByUuid(userUuid))
        .thenReturn(Optional.of(existingUser));

    Mockito.when(usersTypeRepository.findByUuid(newTypeUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(userUuid, input)
    );

    Assertions.assertEquals("User type not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .findByUuid(userUuid);
    Mockito.verify(existingUser, Mockito.times(2))
        .getUsersType();

    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(newTypeUuid);

    Mockito.verifyNoInteractions(mapper);
    Mockito.verify(repository, Mockito.never())
        .update(Mockito.any(), Mockito.any());

    Mockito.verifyNoMoreInteractions(repository, usersTypeRepository, existingUser);
  }
}
