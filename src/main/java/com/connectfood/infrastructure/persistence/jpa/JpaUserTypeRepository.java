package com.connectfood.infrastructure.persistence.jpa;

import com.connectfood.infrastructure.persistence.entity.UserTypeEntity;
import com.connectfood.infrastructure.persistence.jpa.commons.JpaCommonRepository;

public interface JpaUserTypeRepository extends JpaCommonRepository<UserTypeEntity, Long> {

  boolean existsByName(String name);
}
