package com.connectfood.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.infrastructure.persistence.entity.UserAddressEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUserAddressRepository extends JpaCommonRepository<UserAddressEntity, Long> {

  Optional<UserAddressEntity> findByUsersUuid(UUID usersUuid);
}
