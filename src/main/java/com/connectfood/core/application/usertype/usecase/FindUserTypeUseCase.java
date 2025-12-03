package com.connectfood.core.application.usertype.usecase;

import java.util.UUID;

import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersTypeRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FindUserTypeUseCase {

  private final UsersTypeRepository repository;
  private final UsersTypeAppMapper mapper;

  public FindUserTypeUseCase(final UsersTypeRepository repository, final UsersTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public UsersTypeOutput execute(final UUID uuid) {
    final var usersType = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users type not found"));

    return mapper.toOutput(usersType);
  }
}
