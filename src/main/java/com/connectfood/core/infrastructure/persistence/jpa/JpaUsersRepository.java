package com.connectfood.core.infrastructure.persistence.jpa;

import com.connectfood.core.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.core.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUsersRepository extends JpaCommonRepository<UsersEntity, Long> {

  boolean existsByEmail(String email);
}
