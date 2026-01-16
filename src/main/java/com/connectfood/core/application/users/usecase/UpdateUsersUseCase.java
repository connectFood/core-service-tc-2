package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUsersUseCase {

  private final UserGateway repository;
  private final UsersAppMapper mapper;
  private final UserTypeGateway userTypeGateway;

  public UpdateUsersUseCase(
      final UserGateway repository,
      final UsersAppMapper mapper,
      final UserTypeGateway userTypeGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.userTypeGateway = userTypeGateway;
  }

  @Transactional
  public UsersOutput execute(final UUID uuid, final UsersInput input) {
    final var users = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    UserType userType = users.getUserType();

    if (!users.getUserType()
        .getUuid()
        .equals(input.getUsersTypeUuid())) {
      userType = userTypeGateway.findByUuid(input.getUsersTypeUuid())
          .orElseThrow(() -> new NotFoundException("User type not found"));
    }

    final var usersUpdated = repository.update(uuid, mapper.toDomain(uuid, input, users.getPasswordHash(), userType));

    return mapper.toOutput(usersUpdated);
  }
}
