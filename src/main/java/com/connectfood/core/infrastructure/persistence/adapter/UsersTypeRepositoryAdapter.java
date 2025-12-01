package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersType;
import com.connectfood.core.domain.repository.UsersTypeRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersTypeRepository;
import com.connectfood.core.infrastructure.persistence.mappers.UsersTypeInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class UsersTypeRepositoryAdapter implements UsersTypeRepository {

  private final JpaUsersTypeRepository repository;
  private final UsersTypeInfraMapper mapper;

  public UsersTypeRepositoryAdapter(final JpaUsersTypeRepository repository, final UsersTypeInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public UsersType save(final UsersType usersType) {
    final var entity = repository.save(mapper.toEntity(usersType));

    return mapper.toDomain(entity);
  }

  @Override
  public UsersType update(final UUID uuid, final UsersType usersType) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(usersType, entity));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<UsersType> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public List<UsersType> findAll() {
    final var entities = repository.findAll();

    return entities.stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
