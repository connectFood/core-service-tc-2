package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersAddress;

public interface UsersAddressGateway {

  UsersAddress save(UsersAddress usersAddress);

  Optional<UsersAddress> findByUsersUuid(UUID usersUuid);

  void delete(UUID uuid);
}
