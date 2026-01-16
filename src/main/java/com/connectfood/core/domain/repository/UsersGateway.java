package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.model.commons.PageModel;

public interface UsersGateway {

  Users save(Users users);

  Users update(UUID uuid, Users users);

  Optional<Users> findByUuid(UUID uuid);

  PageModel<List<Users>> findAll(String fullName, String email, UUID usersTypeUuid, Integer page, Integer size,
      String sort, String direction);

  void delete(UUID uuid);

  boolean existsByEmail(String email);
}
