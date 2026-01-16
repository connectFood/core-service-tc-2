package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.UserEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUserRepository extends JpaCommonRepository<UserEntity, Long> {

  boolean existsByEmail(String email);
}
