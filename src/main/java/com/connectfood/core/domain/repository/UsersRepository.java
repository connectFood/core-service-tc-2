package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Users;

public interface UsersRepository {

  Users save(Users usersType);

  Optional<Users> findByUUID(UUID uuid);

  List<Users> findAll();

  void delete(UUID uuid);
}
