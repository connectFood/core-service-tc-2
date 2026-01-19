package com.connectfood.core.application.usertype.usecase;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.application.usertype.mapper.UserTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindUserTypeUseCase {

  private final UserTypeGateway repository;
  private final UserTypeAppMapper mapper;

  public FindUserTypeUseCase(final UserTypeGateway repository, final UserTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public UserTypeOutput execute(final UUID uuid) {
    final var usersType = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users type not found"));

    return mapper.toOutput(usersType);
  }
}
