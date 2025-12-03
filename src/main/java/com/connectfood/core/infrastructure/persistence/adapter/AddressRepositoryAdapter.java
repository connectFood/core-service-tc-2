package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.core.infrastructure.persistence.mappers.AddressInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class AddressRepositoryAdapter implements AddressRepository {

  private final JpaAddressRepository repository;
  private final AddressInfraMapper mapper;

  public AddressRepositoryAdapter(final JpaAddressRepository repository, final AddressInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Address save(final Address usersType) {
    final var entity = repository.save(mapper.toEntity(usersType));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<Address> findByUuid(final UUID uuid) {
    final var entity = repository.findByUuid(uuid);

    return entity.map(mapper::toDomain);
  }

  @Override
  public List<Address> findAll() {
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
