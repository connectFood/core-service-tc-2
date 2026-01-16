package com.connectfood.core.application.users.usecase;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersGateway;
import com.connectfood.core.domain.repository.UsersTypeGateway;
import com.connectfood.core.domain.utils.PasswordGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUsersUseCase {

  private final UsersGateway repository;
  private final UsersAppMapper mapper;
  private final UsersTypeGateway usersTypeGateway;
  private final PasswordGateway passwordGateway;
  private final CreateUsersAddressUseCase createUsersAddressUseCase;

  public CreateUsersUseCase(
      final UsersGateway repository,
      final UsersAppMapper mapper,
      final UsersTypeGateway usersTypeGateway,
      final PasswordGateway passwordGateway,
      final CreateUsersAddressUseCase createUsersAddressUseCase) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersTypeGateway = usersTypeGateway;
    this.passwordGateway = passwordGateway;
    this.createUsersAddressUseCase = createUsersAddressUseCase;
  }

  @Transactional
  public UsersOutput execute(final UsersInput input) {
    validateUsersExists(input.getEmail());

    final var passwordHash = passwordGateway.encode(input.getPassword());

    final var usersType =
        usersTypeGateway.findByUuid(input.getUsersTypeUuid())
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
