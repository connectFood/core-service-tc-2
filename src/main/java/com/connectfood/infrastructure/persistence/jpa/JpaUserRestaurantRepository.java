package com.connectfood.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.UserRestaurantEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUserRestaurantRepository extends JpaCommonRepository<UserRestaurantEntity, Long> {

  Optional<UserRestaurantEntity> findByUsersUuid(UUID usersUuid);
}
