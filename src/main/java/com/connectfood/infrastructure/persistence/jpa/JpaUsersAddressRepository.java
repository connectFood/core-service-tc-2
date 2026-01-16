package com.connectfood.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.UsersAddressEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUsersAddressRepository extends JpaCommonRepository<UsersAddressEntity, Long> {

  Optional<UsersAddressEntity> findByUsersUuid(UUID usersUuid);
}
