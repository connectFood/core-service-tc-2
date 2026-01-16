package com.connectfood.core.application.user.usecase;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.user.dto.UserInput;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserTypeGateway;
import com.connectfood.core.domain.utils.PasswordGateway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

  @Mock
  private UserGateway repository;

  @Mock
  private UserAppMapper mapper;

  @Mock
  private UserTypeGateway userTypeGateway;

  @Mock
  private PasswordGateway passwordGateway;

  @Mock
  private CreateUserAddressUseCase createUserAddressUseCase;

  @InjectMocks
  private CreateUserUseCase useCase;

  @Test
  @DisplayName("Deve criar usuário com sucesso quando tipo existir e usuário não existir")
  void shouldCreateUserSuccessfullyWhenUsersTypeExists() {
    final var usersTypeUuid = UUID.randomUUID();

    final UserInput input = Mockito.mock(UserInput.class);
    Mockito.when(input.getEmail())
        .thenReturn("user@test.com");
    Mockito.when(input.getPassword())
        .thenReturn("senha123");
    Mockito.when(input.getUsersTypeUuid())
        .thenReturn(usersTypeUuid);

    Mockito.when(repository.existsByEmail("user@test.com"))
        .thenReturn(false);

    Mockito.when(passwordGateway.encode("senha123"))
        .thenReturn("hashSenha");

    final UserType userType = Mockito.mock(UserType.class);
    Mockito.when(userTypeGateway.findByUuid(usersTypeUuid))
        .thenReturn(Optional.of(userType));

    final User domainToSave = Mockito.mock(User.class);
    Mockito.when(mapper.toDomain(input, "hashSenha", userType))
        .thenReturn(domainToSave);

    final User savedUser = Mockito.mock(User.class);
    final var savedUserUuid = UUID.randomUUID();
    Mockito.when(savedUser.getUuid())
        .thenReturn(savedUserUuid);

    Mockito.when(repository.save(domainToSave))
        .thenReturn(savedUser);

    final AddressInput addressInput = Mockito.mock(AddressInput.class);
    Mockito.when(input.getAddress())
        .thenReturn(addressInput);

    final AddressOutput addressOutput = Mockito.mock(AddressOutput.class);
    Mockito.when(createUserAddressUseCase.execute(savedUserUuid, addressInput))
        .thenReturn(addressOutput);

    final UserOutput expectedOutput = Mockito.mock(UserOutput.class);
    Mockito.when(mapper.toOutput(savedUser, addressOutput))
        .thenReturn(expectedOutput);

    final var result = useCase.execute(input);

    Assertions.assertNotNull(result);
    Assertions.assertSame(expectedOutput, result);

    Mockito.verify(repository, Mockito.times(1))
        .existsByEmail("user@test.com");
    Mockito.verify(passwordGateway, Mockito.times(1))
        .encode("senha123");
    Mockito.verify(userTypeGateway, Mockito.times(1))
        .findByUuid(usersTypeUuid);
    Mockito.verify(mapper, Mockito.times(1))
        .toDomain(input, "hashSenha", userType);
    Mockito.verify(repository, Mockito.times(1))
        .save(domainToSave);
    Mockito.verify(savedUser, Mockito.times(1))
        .getUuid();
    Mockito.verify(createUserAddressUseCase, Mockito.times(1))
        .execute(savedUserUuid, addressInput);
    Mockito.verify(mapper, Mockito.times(1))
        .toOutput(savedUser, addressOutput);

    Mockito.verifyNoMoreInteractions(
        repository,
        mapper,
        userTypeGateway,
        passwordGateway,
        createUserAddressUseCase,
        savedUser
    );
  }

  @Test
  @DisplayName("Deve lançar NotFoundException quando tipo de usuário não existir")
  void shouldThrowNotFoundExceptionWhenUsersTypeDoesNotExist() {
    final var usersTypeUuid = UUID.randomUUID();

    final UserInput input = Mockito.mock(UserInput.class);
    Mockito.when(input.getEmail())
        .thenReturn("user@test.com");
    Mockito.when(input.getPassword())
        .thenReturn("senha123");
    Mockito.when(input.getUsersTypeUuid())
        .thenReturn(usersTypeUuid);

    Mockito.when(repository.existsByEmail("user@test.com"))
        .thenReturn(false);

    Mockito.when(passwordGateway.encode("senha123"))
        .thenReturn("hashSenha");

    Mockito.when(userTypeGateway.findByUuid(usersTypeUuid))
        .thenReturn(Optional.empty());

    final var ex = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(input)
    );

    Assertions.assertEquals("Users type not found", ex.getMessage());

    Mockito.verify(repository, Mockito.times(1))
        .existsByEmail("user@test.com");
    Mockito.verify(passwordGateway, Mockito.times(1))
        .encode("senha123");
    Mockito.verify(userTypeGateway, Mockito.times(1))
        .findByUuid(usersTypeUuid);

    Mockito.verifyNoInteractions(mapper, createUserAddressUseCase);
    Mockito.verify(repository, Mockito.never())
        .save(Mockito.any());

    Mockito.verifyNoMoreInteractions(repository, userTypeGateway, passwordGateway);
  }

  @Test
  @DisplayName("Deve lançar ConflictException quando usuário já existir pelo email")
  void shouldThrowConflictExceptionWhenUserAlreadyExists() {
    final UserInput input = Mockito.mock(UserInput.class);
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

    Mockito.verifyNoInteractions(passwordGateway, userTypeGateway, mapper, createUserAddressUseCase);
    Mockito.verify(repository, Mockito.never())
        .save(Mockito.any());

    Mockito.verifyNoMoreInteractions(repository);
  }
}
