package com.connectfood.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UsersAddress;
import com.connectfood.core.domain.repository.UsersAddressGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUsersRepository;
import com.connectfood.infrastructure.persistence.mappers.UsersAddressInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class UsersAddressGatewayAdapter implements UsersAddressGateway {

  private final JpaUsersAddressRepository repository;
  private final UsersAddressInfraMapper mapper;
  private final JpaUsersRepository usersRepository;
  private final JpaAddressRepository addressRepository;

  public UsersAddressGatewayAdapter(final JpaUsersAddressRepository repository,
                                    final UsersAddressInfraMapper mapper,
                                    final JpaUsersRepository usersRepository,
                                    final JpaAddressRepository addressRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersRepository = usersRepository;
    this.addressRepository = addressRepository;
  }

  @Override
  public UsersAddress save(final UsersAddress usersAddress) {
    final var users = usersRepository.findByUuid(usersAddress.getUsers()
            .getUuid())
        .orElseThrow();
    final var address = addressRepository.findByUuid(usersAddress.getAddress()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(usersAddress.getUuid(), users, address));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<UsersAddress> findByUsersUuid(final UUID usersUuid) {
    final var entity = repository.findByUsersUuid(usersUuid);

    return entity.map(mapper::toDomain);
  }


  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
