package com.connectfood.core.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.model.commons.PageModel;

public interface UsersTypeRepository {

  UsersType save(UsersType usersType);

  UsersType update(UUID uuid, UsersType usersType);

  Optional<UsersType> findByUuid(UUID uuid);

  PageModel<List<UsersType>> findAll(String name, Integer page, Integer size, String sort, String direction);

  void delete(UUID uuid);
}
