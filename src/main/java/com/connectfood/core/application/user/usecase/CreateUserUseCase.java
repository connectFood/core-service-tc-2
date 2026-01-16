package com.connectfood.core.application.user.usecase;

import com.connectfood.core.application.user.dto.UserInput;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserTypeGateway;
import com.connectfood.core.domain.utils.PasswordGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUserUseCase {

  private final UserGateway repository;
  private final UserAppMapper mapper;
  private final UserTypeGateway userTypeGateway;
  private final PasswordGateway passwordGateway;
  private final CreateUserAddressUseCase createUserAddressUseCase;

  public CreateUserUseCase(
      final UserGateway repository,
      final UserAppMapper mapper,
      final UserTypeGateway userTypeGateway,
      final PasswordGateway passwordGateway,
      final CreateUserAddressUseCase createUserAddressUseCase) {
    this.repository = repository;
    this.mapper = mapper;
    this.userTypeGateway = userTypeGateway;
    this.passwordGateway = passwordGateway;
    this.createUserAddressUseCase = createUserAddressUseCase;
  }

  @Transactional
  public UserOutput execute(final UserInput input) {
    validateUsersExists(input.getEmail());

    final var passwordHash = passwordGateway.encode(input.getPassword());

    final var usersType =
        userTypeGateway.findByUuid(input.getUsersTypeUuid())
            .orElseThrow(() -> new NotFoundException("Users type not found"));

    final var users = repository.save(mapper.toDomain(input, passwordHash, usersType));

    final var address = createUserAddressUseCase.execute(users.getUuid(), input.getAddress());

    return mapper.toOutput(users, address);
  }

  private void validateUsersExists(final String email) {
    final var usersExists = repository.existsByEmail(email);

    if (usersExists) {
      throw new ConflictException("User already exists");
    }
  }
}
