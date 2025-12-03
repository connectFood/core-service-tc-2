package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersTypeRepository;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class UpdateUserTypeUseCase {

  private final UsersTypeRepository repository;
  private final UsersAppMapper mapper;

  public UpdateUserTypeUseCase(final UsersTypeRepository repository, final UsersAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public UsersOutput execute(final UUID uuid, final UsersInput input) {
    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("User type not found"));

    final var usersType = repository.update(uuid, mapper.toDomain(uuid, input));

    return mapper.toOutput(usersType);
  }
}
