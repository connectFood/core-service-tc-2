package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.Users;

public interface AddressRepository {

  List<Address> findAllByUserUuid(String uuid);

  Address save(Address address, String userUuid);

  void deleteByUserUuid(String userUuid);
}
