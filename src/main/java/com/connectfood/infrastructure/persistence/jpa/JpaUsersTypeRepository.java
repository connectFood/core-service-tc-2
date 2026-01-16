package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.UsersTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUsersTypeRepository extends JpaCommonRepository<UsersTypeEntity, Long> {

  boolean existsByName(String name);
}
