package com.connectfood.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.Address;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.infrastructure.persistence.mappers.AddressInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class AddressGatewayAdapter implements AddressGateway {

  private final JpaAddressRepository repository;
  private final AddressInfraMapper mapper;

  public AddressGatewayAdapter(final JpaAddressRepository repository, final AddressInfraMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Address save(final Address address) {
    final var entity = repository.save(mapper.toEntity(address));

    return mapper.toDomain(entity);
  }

  @Override
  public Address update(UUID uuid, Address model) {
    var entity = repository.findByUuid(uuid)
        .orElseThrow();

    entity = repository.save(mapper.toEntity(model, entity));

    return mapper.toDomain(entity);
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
