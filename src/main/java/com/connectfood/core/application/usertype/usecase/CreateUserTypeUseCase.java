package com.connectfood.core.application.usertype.usecase;

import com.connectfood.core.application.usertype.dto.UserTypeInput;
import com.connectfood.core.application.usertype.dto.UserTypeOutput;
import com.connectfood.core.application.usertype.mapper.UserTypeAppMapper;
import com.connectfood.core.domain.exception.ConflictException;
import com.connectfood.core.domain.repository.UserTypeGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUserTypeUseCase {

  private final UserTypeGateway repository;
  private final UserTypeAppMapper mapper;


  public CreateUserTypeUseCase(final UserTypeGateway repository, final UserTypeAppMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Transactional
  public UserTypeOutput execute(final UserTypeInput input) {
    validateUserTypeExists(input.getName());
    final var usersType = repository.save(mapper.toDomain(input));

    return mapper.toOutput(usersType);
  }

  private void validateUserTypeExists(final String name) {
    final var exists = repository.existsByName(name);

    if (exists) {
      throw new ConflictException("User type already exists");
    }
  }
}
