package com.connectfood.core.application.user.usecase;

import java.util.UUID;

import com.connectfood.core.application.user.dto.UserInput;
import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.repository.UserGateway;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUserUseCase {

  private final UserGateway repository;
  private final UserAppMapper mapper;
  private final UserTypeGateway userTypeGateway;

  public UpdateUserUseCase(
      final UserGateway repository,
      final UserAppMapper mapper,
      final UserTypeGateway userTypeGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.userTypeGateway = userTypeGateway;
  }

  @Transactional
  public UserOutput execute(final UUID uuid, final UserInput input) {
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
