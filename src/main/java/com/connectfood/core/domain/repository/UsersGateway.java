package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.User;
import com.connectfood.core.domain.model.commons.PageModel;

public interface UsersGateway {

  User save(User user);

  User update(UUID uuid, User user);

  Optional<User> findByUuid(UUID uuid);

  PageModel<List<User>> findAll(String fullName, String email, UUID usersTypeUuid, Integer page, Integer size,
      String sort, String direction);

  void delete(UUID uuid);

  boolean existsByEmail(String email);
}
