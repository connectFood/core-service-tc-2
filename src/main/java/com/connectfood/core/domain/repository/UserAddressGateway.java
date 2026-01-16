package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UserAddress;

public interface UserAddressGateway {

  UserAddress save(UserAddress userAddress);

  Optional<UserAddress> findByUsersUuid(UUID usersUuid);

  void delete(UUID uuid);
}
