package com.connectfood.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import com.connectfood.core.domain.model.UserAddress;
import com.connectfood.core.domain.repository.UserAddressGateway;
import com.connectfood.infrastructure.persistence.jpa.JpaAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUserAddressRepository;
import com.connectfood.infrastructure.persistence.jpa.JpaUserRepository;
import com.connectfood.infrastructure.persistence.mappers.UserAddressInfraMapper;

import org.springframework.stereotype.Repository;

@Repository
public class UserAddressGatewayAdapter implements UserAddressGateway {

  private final JpaUserAddressRepository repository;
  private final UserAddressInfraMapper mapper;
  private final JpaUserRepository usersRepository;
  private final JpaAddressRepository addressRepository;

  public UserAddressGatewayAdapter(final JpaUserAddressRepository repository,
                                   final UserAddressInfraMapper mapper,
                                   final JpaUserRepository usersRepository,
                                   final JpaAddressRepository addressRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersRepository = usersRepository;
    this.addressRepository = addressRepository;
  }

  @Override
  public UserAddress save(final UserAddress userAddress) {
    final var users = usersRepository.findByUuid(userAddress.getUser()
            .getUuid())
        .orElseThrow();
    final var address = addressRepository.findByUuid(userAddress.getAddress()
            .getUuid())
        .orElseThrow();

    final var entity = repository.save(mapper.toEntity(userAddress.getUuid(), users, address));

    return mapper.toDomain(entity);
  }

  @Override
  public Optional<UserAddress> findByUsersUuid(final UUID usersUuid) {
    final var entity = repository.findByUsersUuid(usersUuid);

    return entity.map(mapper::toDomain);
  }


  @Override
  public void delete(final UUID uuid) {
    repository.deleteByUuid(uuid);
  }
}
