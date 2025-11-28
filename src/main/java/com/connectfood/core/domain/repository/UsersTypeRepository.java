package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersType;

public interface UsersTypeRepository {

  UsersType save(UsersType usersType);

  Optional<UsersType> findByUUID(UUID uuid);

  List<UsersType> findAll();

  void delete(UUID uuid);
}
