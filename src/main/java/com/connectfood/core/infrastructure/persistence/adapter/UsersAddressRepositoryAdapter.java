package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersAddress;
import com.connectfood.core.domain.repository.UsersAddressRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaUsersAddressRepository;
import com.connectfood.core.infrastructure.persistence.mappers.UsersAddressInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class UsersAddressRepositoryAdapter implements UsersAddressRepository {

  private final JpaUsersAddressRepository repository;
  private final UsersAddressInfraMapper mapper;

  public UsersAddressRepositoryAdapter(final JpaUsersAddressRepository repository,
      final UsersAddressInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public UsersAddress save(final UsersAddress usersType) {
    final var entity = repository.save(mapper.toEntity(usersType));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<UsersAddress> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public List<UsersAddress> findAll() {
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
