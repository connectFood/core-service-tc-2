package com.connectfood.core.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.RestaurantsAddress;

public interface AddressRepository {

  Address save(Address usersType);

  Address update(UUID uuid, Address address);

  Optional<Address> findByUuid(UUID uuid);

  void delete(UUID uuid);
}
