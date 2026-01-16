package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.users.mapper.UsersAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.UsersAddressGateway;
import com.connectfood.core.domain.repository.UsersGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUsersAddressUseCase {

  private final AddressGateway repository;
  private final AddressAppMapper mapper;
  private final UsersGateway usersGateway;
  private final UsersAddressGateway usersAddressGateway;
  private final UsersAddressAppMapper usersAddressMapper;

  public CreateUsersAddressUseCase(
      final AddressGateway repository,
      final AddressAppMapper mapper,
      final UsersGateway usersGateway,
      final UsersAddressGateway usersAddressGateway,
      final UsersAddressAppMapper usersAddressMapper) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersGateway = usersGateway;
    this.usersAddressGateway = usersAddressGateway;
    this.usersAddressMapper = usersAddressMapper;
  }

  @Transactional
  public AddressOutput execute(final UUID userUuid, final AddressInput input) {
    final var users = usersGateway.findByUuid(userUuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var address = repository.save(mapper.toDomain(input));

    final var usersAddress = usersAddressGateway.save(usersAddressMapper.toDomain(users, address));

    return mapper.toOutput(usersAddress.getAddress());
  }
}
