package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersAddressGateway;
import com.connectfood.core.domain.repository.UsersGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FindUsersUseCase {

  private final UsersGateway repository;
  private final UsersAddressGateway usersAddressGateway;
  private final UsersAppMapper mapper;

  public FindUsersUseCase(
      final UsersGateway repository,
      final UsersAppMapper mapper,
      final UsersAddressGateway usersAddressGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersAddressGateway = usersAddressGateway;
  }

  @Transactional(readOnly = true)
  public UsersOutput execute(final UUID uuid) {
    final var users = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users not found"));

    final var usersAddress = usersAddressGateway.findByUsersUuid(users.getUuid());

    if (usersAddress.isPresent()) {
      return mapper.toOutput(users, usersAddress.get()
          .getAddress()
      );
    }

    return mapper.toOutput(users);
  }
}
