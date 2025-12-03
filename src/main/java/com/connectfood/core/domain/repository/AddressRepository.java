package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Address;

public interface AddressRepository {

  Address save(Address usersType);

  Optional<Address> findByUuid(UUID uuid);

  List<Address> findAll();

  void delete(UUID uuid);
}
