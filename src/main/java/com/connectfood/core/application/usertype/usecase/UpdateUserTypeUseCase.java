package com.connectfood.core.application.usertype.usecase;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUserTypeUseCase {

  private final UserTypeGateway repository;
  private final UsersTypeAppMapper mapper;

  public UpdateUserTypeUseCase(final UserTypeGateway repository, final UsersTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public UsersTypeOutput execute(final UUID uuid, final UsersTypeInput input) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User type not found"));

    final var usersType = repository.update(uuid, mapper.toDomain(uuid, input));

    return mapper.toOutput(usersType);
  }
}
