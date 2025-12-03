package com.connectfood.core.application.users.usecase;

import com.connectfood.core.application.users.dto.UsersInput;
import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.domain.repository.UsersTypeRepository;
import com.connectfood.core.domain.utils.PasswordUtils;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUsersUseCase {

  private final UsersRepository repository;
  private final UsersAppMapper mapper;
  private final UsersTypeRepository usersTypeRepository;
  private final PasswordUtils passwordUtils;

  public CreateUsersUseCase(
      final UsersRepository repository,
      final UsersAppMapper mapper,
      final UsersTypeRepository usersTypeRepository,
      final PasswordUtils passwordUtils) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersTypeRepository = usersTypeRepository;
    this.passwordUtils = passwordUtils;
  }

  @Transactional
  public UsersOutput execute(final UsersInput input) {

    final var passwordHash = passwordUtils.encode(input.getPassword());

    final var usersType =
        usersTypeRepository.findByUuid(input.getUsersTypeUuid())
            .orElseThrow(() -> new NotFoundException("Users type not found"));

    final var users = repository.save(mapper.toDomain(input, passwordHash, usersType));

    return mapper.toOutput(users);
  }
}
