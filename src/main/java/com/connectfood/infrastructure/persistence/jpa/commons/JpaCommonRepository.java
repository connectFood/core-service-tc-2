package com.connectfood.infrastructure.persistence.jpa.commons;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JpaCommonRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

  Optional<T> findByUuid(UUID uuid);

  void deleteByUuid(UUID uuid);
}
