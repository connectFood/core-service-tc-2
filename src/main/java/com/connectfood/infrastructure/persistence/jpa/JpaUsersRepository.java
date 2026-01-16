package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.UsersEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUsersRepository extends JpaCommonRepository<UsersEntity, Long> {

  boolean existsByEmail(String email);
}
