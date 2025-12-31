package com.connectfood.core.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.model.RestaurantsAddress;
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
  public Address save(final Address address) {
    final var entity = repository.save(mapper.toEntity(address));

    return mapper.toDomain(entity);
  }

  @Override
  public RestaurantsAddress update(UUID uuid, Address address) {
    return null;
  }

  @Override
  public Optional<Address> findByUuid(UUID uuid) {
    return Optional.empty();
  }

  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
