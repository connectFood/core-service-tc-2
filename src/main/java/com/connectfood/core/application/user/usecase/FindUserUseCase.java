package com.connectfood.core.application.user.usecase;

import java.util.UUID;

import com.connectfood.core.application.user.dto.UserOutput;
import com.connectfood.core.application.user.mapper.UserAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.UserAddressGateway;
import com.connectfood.core.domain.repository.UserGateway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindUserUseCase {

  private final UserGateway repository;
  private final UserAddressGateway userAddressGateway;
  private final UserAppMapper mapper;

  public FindUserUseCase(
      final UserGateway repository,
      final UserAppMapper mapper,
      final UserAddressGateway userAddressGateway) {
    this.repository = repository;
    this.mapper = mapper;
    this.userAddressGateway = userAddressGateway;
  }

  @Transactional(readOnly = true)
  public UserOutput execute(final UUID uuid) {
    final var users = repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Users not found"));

    final var usersAddress = userAddressGateway.findByUsersUuid(users.getUuid());

    if (usersAddress.isPresent()) {
      return mapper.toOutput(users, usersAddress.get()
          .getAddress()
      );
    }

    return mapper.toOutput(users);
  }
}
