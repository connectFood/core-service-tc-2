package com.connectfood.core.application.usertype.usecase;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UserTypeInput;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.application.usertype.mapper.UserTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUserTypeUseCase {

  private final UserTypeGateway repository;
  private final UserTypeAppMapper mapper;

  public UpdateUserTypeUseCase(final UserTypeGateway repository, final UserTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public UserTypeOutput execute(final UUID uuid, final UserTypeInput input) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User type not found"));

    final var usersType = repository.update(uuid, mapper.toDomain(uuid, input));

    return mapper.toOutput(usersType);
  }
}
