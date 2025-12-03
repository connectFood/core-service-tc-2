package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersTypeRepository;

import org.springframework.stereotype.Component;

@Component
public class FindUserTypeUseCase {

  private final UsersTypeRepository repository;
  private final UsersAppMapper mapper;

  public FindUserTypeUseCase(final UsersTypeRepository repository, final UsersAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public UsersOutput execute(final UUID uuid) {
    final var usersType = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users type not found"));

    return mapper.toOutput(usersType);
  }
}
