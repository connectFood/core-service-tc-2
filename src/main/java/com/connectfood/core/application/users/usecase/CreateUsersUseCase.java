package com.connectfood.core.application.users.usecase;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserTypeGateway;
import com.connectfood.core.domain.utils.PasswordGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUsersUseCase {

  private final UserGateway repository;
  private final UsersAppMapper mapper;
  private final UserTypeGateway userTypeGateway;
  private final PasswordGateway passwordGateway;
  private final CreateUsersAddressUseCase createUsersAddressUseCase;

  public CreateUsersUseCase(
      final UserGateway repository,
      final UsersAppMapper mapper,
      final UserTypeGateway userTypeGateway,
      final PasswordGateway passwordGateway,
      final CreateUsersAddressUseCase createUsersAddressUseCase) {
    this.repository = repository;
    this.mapper = mapper;
    this.userTypeGateway = userTypeGateway;
    this.passwordGateway = passwordGateway;
    this.createUsersAddressUseCase = createUsersAddressUseCase;
  }

  @Transactional
  public UsersOutput execute(final UsersInput input) {
    validateUsersExists(input.getEmail());

    final var passwordHash = passwordGateway.encode(input.getPassword());

    final var usersType =
        userTypeGateway.findByUuid(input.getUsersTypeUuid())
            .orElseThrow(() -> new NotFoundException("Users type not found"));

    final var users = repository.save(mapper.toDomain(input, passwordHash, usersType));

    final var address = createUsersAddressUseCase.execute(users.getUuid(), input.getAddress());

    return mapper.toOutput(users, address);
  }

  private void validateUsersExists(final String email) {
    final var usersExists = repository.existsByEmail(email);

    if (usersExists) {
      throw new ConflictException("User already exists");
    }
  }
}
