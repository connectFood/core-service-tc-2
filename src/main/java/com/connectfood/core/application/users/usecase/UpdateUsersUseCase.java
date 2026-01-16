package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.repository.UsersGateway;
import com.connectfood.core.domain.repository.UsersTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUsersUseCase {

  private final UsersGateway repository;
  private final UsersAppMapper mapper;
  private final UsersTypeGateway usersTypeGateway;

  public UpdateUsersUseCase(
      final UsersGateway repository,
      final UsersAppMapper mapper,
      final UsersTypeGateway usersTypeGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersTypeGateway = usersTypeGateway;
  }

  @Transactional
  public UsersOutput execute(final UUID uuid, final UsersInput input) {
    final var users = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    UsersType usersType = users.getUsersType();

    if (!users.getUsersType()
        .getUuid()
        .equals(input.getUsersTypeUuid())) {
      usersType = usersTypeGateway.findByUuid(input.getUsersTypeUuid())
          .orElseThrow(() -> new NotFoundException("User type not found"));
    }

    final var usersUpdated = repository.update(uuid, mapper.toDomain(uuid, input, users.getPasswordHash(), usersType));

    return mapper.toOutput(usersUpdated);
  }
}
