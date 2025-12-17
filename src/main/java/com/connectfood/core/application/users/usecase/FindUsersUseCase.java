package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.users.dto.UsersOutput;
import com.connectfood.core.application.users.mapper.UsersAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UsersAddressRepository;
import com.connectfood.core.domain.repository.UsersRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FindUsersUseCase {

  private final UsersRepository repository;
  private final UsersAddressRepository usersAddressRepository;
  private final UsersAppMapper mapper;

  public FindUsersUseCase(
      final UsersRepository repository,
      final UsersAppMapper mapper,
      final UsersAddressRepository usersAddressRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersAddressRepository = usersAddressRepository;
  }

  @Transactional(readOnly = true)
  public UsersOutput execute(final UUID uuid) {
    final var users = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users not found"));

    final var usersAddress = usersAddressRepository.findByUsersUuid(users.getUuid());

    if (usersAddress.isPresent()) {
      return mapper.toOutput(users, usersAddress.get()
          .getAddress()
      );
    }

    return mapper.toOutput(users);
  }
}
