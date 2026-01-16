package com.connectfood.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.UsersRestaurantEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUsersRestaurantRepository extends JpaCommonRepository<UsersRestaurantEntity, Long> {

  Optional<UsersRestaurantEntity> findByUsersUuid(UUID usersUuid);
}
