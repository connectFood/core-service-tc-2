package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Users;
import com.connectfood.core.domain.repository.UsersRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.core.infrastructure.persistence.mappers.UsersInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class UsersRepositoryAdapter implements UsersRepository {

  private final JpaUsersRepository repository;
  private final UsersInfraMapper mapper;

  public UsersRepositoryAdapter(final JpaUsersRepository repository, final UsersInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Users save(final Users usersType) {
    final var entity = repository.save(mapper.toEntity(usersType));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<Users> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public List<Users> findAll() {
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
