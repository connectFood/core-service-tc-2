package com.connectfood.core.application.usertype.usecase;

import com.connectfood.core.application.usertype.dto.UsersTypeInput;
import com.connectfood.core.application.usertype.dto.UsersTypeOutput;
import com.connectfood.core.application.usertype.mapper.UsersTypeAppMapper;
import com.connectfood.core.domain.repository.UsersTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUserTypeUseCase {

  private final UsersTypeGateway repository;
  private final UsersTypeAppMapper mapper;


  public CreateUserTypeUseCase(final UsersTypeGateway repository, final UsersTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public UsersTypeOutput execute(final UsersTypeInput input) {
    final var usersType = repository.save(mapper.toDomain(input));

    return mapper.toOutput(usersType);
  }
}
