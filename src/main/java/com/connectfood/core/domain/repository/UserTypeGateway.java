package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UserType;
import com.connectfood.core.domain.model.commons.PageModel;

public interface UserTypeGateway {

  UserType save(UserType userType);

  UserType update(UUID uuid, UserType userType);

  Optional<UserType> findByUuid(UUID uuid);

  PageModel<List<UserType>> findAll(String name, Integer page, Integer size, String sort, String direction);

  void delete(UUID uuid);

  boolean existsByName(String name);
}
