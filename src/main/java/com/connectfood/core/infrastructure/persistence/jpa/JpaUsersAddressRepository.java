package com.connectfood.core.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.infrastructure.persistence.entity.UsersAddressEntity;
import com.connectfood.core.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUsersAddressRepository extends JpaCommonRepository<UsersAddressEntity, Long> {

  Optional<UsersAddressEntity> findByUsersUuid(UUID usersUuid);
}
