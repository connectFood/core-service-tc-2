package com.connectfood.core.application.users.usecase;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.repository.UsersTypeRepository;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class CreateUserTypeUseCase {

  private final UsersTypeRepository repository;
  private final UsersAppMapper mapper;


  public CreateUserTypeUseCase(final UsersTypeRepository repository, final UsersAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public UsersOutput execute(final UsersInput input) {
    final var usersType = repository.save(mapper.toDomain(input));

    return mapper.toOutput(usersType);
  }
}
