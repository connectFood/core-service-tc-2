package com.connectfood.core.application.users.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.repository.UsersTypeRepository;
import com.connectfood.core.domain.utils.PasswordUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUsersUseCaseTest {

  @Mock
  private UsersRepository repository;

  @Mock
  private UsersAppMapper mapper;

  @Mock
  private UsersTypeRepository usersTypeRepository;

  @Mock
  private PasswordUtils passwordUtils;

  @Mock
  private CreateUsersAddressUseCase createUsersAddressUseCase;

  @InjectMocks
  private CreateUsersUseCase useCase;

  @Test
  @DisplayName("Deve criar usuário com sucesso quando tipo existir e usuário não existir")
  void shouldCreateUserSuccessfullyWhenUsersTypeExists() {
    final var usersTypeUuid = UUID.randomUUID();

    final UsersInput input = Mockito.mock(UsersInput.class);
    Mockito.when(input.getEmail())
        .thenReturn("user@test.com");
    Mockito.when(input.getPassword())
        .thenReturn("senha123");
    Mockito.when(input.getUsersTypeUuid())
        .thenReturn(usersTypeUuid);

    Mockito.when(repository.existsByEmail("user@test.com"))
        .thenReturn(false);

    Mockito.when(passwordUtils.encode("senha123"))
        .thenReturn("hashSenha");

    final UsersType usersType = Mockito.mock(UsersType.class);
    Mockito.when(usersTypeRepository.findByUuid(usersTypeUuid))
        .thenReturn(Optional.of(usersType));

    final Users domainToSave = Mockito.mock(Users.class);
    Mockito.when(mapper.toDomain(input, "hashSenha", usersType))
        .thenReturn(domainToSave);

    final Users savedUsers = Mockito.mock(Users.class);
    final var savedUserUuid = UUID.randomUUID();
    Mockito.when(savedUsers.getUuid())
        .thenReturn(savedUserUuid);

    Mockito.when(repository.save(domainToSave))
        .thenReturn(savedUsers);

    final AddressInput addressInput = Mockito.mock(AddressInput.class);
    Mockito.when(input.getAddress())
        .thenReturn(addressInput);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(createUsersAddressUseCase.execute(savedUserUuid, addressInput))
        .thenReturn(addressOutput);

    final UsersOutput expectedOutput = Mockito.mock(UsersOutput.class);
    Mockito.when(mapper.toOutput(savedUsers, addressOutput))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .existsByEmail("user@test.com");
    Mockito.verify(passwordUtils, Mockito.times(1))
        .encode("senha123");
    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(usersTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, "hashSenha", usersType);
    Mockito.verify(repository, Mockito.times(1))
        .save(domainToSave);
    Mockito.verify(savedUsers, Mockito.times(1))
        .getUuid();
    Mockito.verify(createUsersAddressUseCase, Mockito.times(1))
        .execute(savedUserUuid, addressInput);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedUsers, addressOutput);

    Mockito.verifyNoMoreInteractions(
        repository,
        mapper,
        usersTypeRepository,
        passwordUtils,
        createUsersAddressUseCase,
        savedUsers
    );
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando tipo de usuário não existir")
  void shouldThrowNotFoundExceptionWhenUsersTypeDoesNotExist() {
    final var usersTypeUuid = UUID.randomUUID();

    final UsersInput input = Mockito.mock(UsersInput.class);
    Mockito.when(input.getEmail())
        .thenReturn("user@test.com");
    Mockito.when(input.getPassword())
        .thenReturn("senha123");
    Mockito.when(input.getUsersTypeUuid())
        .thenReturn(usersTypeUuid);

    Mockito.when(repository.existsByEmail("user@test.com"))
        .thenReturn(false);

    Mockito.when(passwordUtils.encode("senha123"))
        .thenReturn("hashSenha");

    Mockito.when(usersTypeRepository.findByUuid(usersTypeUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Users type not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .existsByEmail("user@test.com");
    Mockito.verify(passwordUtils, Mockito.times(1))
        .encode("senha123");
    Mockito.verify(usersTypeRepository, Mockito.times(1))
        .findByUuid(usersTypeUuid);

    Mockito.verifyNoInteractions(mapper, createUsersAddressUseCase);
    Mockito.verify(repository, Mockito.never())
        .save(Mockito.any());

    Mockito.verifyNoMoreInteractions(repository, usersTypeRepository, passwordUtils);
  }

  @Test
  @DisplayName("Deve lançar ConflictException quando usuário já existir pelo email")
  void shouldThrowConflictExceptionWhenUserAlreadyExists() {
    final UsersInput input = Mockito.mock(UsersInput.class);
    Mockito.when(input.getEmail())
        .thenReturn("user@test.com");

    Mockito.when(repository.existsByEmail("user@test.com"))
        .thenReturn(true);

    final var ex = Assertions.assertThrows(
        ConflictException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("User already exists", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .existsByEmail("user@test.com");

    Mockito.verifyNoInteractions(passwordUtils, usersTypeRepository, mapper, createUsersAddressUseCase);
    Mockito.verify(repository, Mockito.never())
        .save(Mockito.any());

    Mockito.verifyNoMoreInteractions(repository);
  }
}
