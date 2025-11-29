package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersAddress;

public interface UsersAddressRepository {

  UsersAddress save(UsersAddress usersType);

  Optional<UsersAddress> findByUuid(UUID uuid);

  List<UsersAddress> findAll();

  void delete(UUID uuid);
}
