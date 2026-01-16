package com.connectfood.core.application.user.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.user.mapper.UserAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.UserAddressGateway;
import com.connectfood.core.domain.repository.UserGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUserAddressUseCase {

  private final AddressGateway repository;
  private final AddressAppMapper mapper;
  private final UserGateway userGateway;
  private final UserAddressGateway userAddressGateway;
  private final UserAddressAppMapper usersAddressMapper;

  public CreateUserAddressUseCase(
      final AddressGateway repository,
      final AddressAppMapper mapper,
      final UserGateway userGateway,
      final UserAddressGateway userAddressGateway,
      final UserAddressAppMapper usersAddressMapper) {
    this.repository = repository;
    this.mapper = mapper;
    this.userGateway = userGateway;
    this.userAddressGateway = userAddressGateway;
    this.usersAddressMapper = usersAddressMapper;
  }

  @Transactional
  public AddressOutput execute(final UUID userUuid, final AddressInput input) {
    final var users = userGateway.findByUuid(userUuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var address = repository.save(mapper.toDomain(input));

    final var usersAddress = userAddressGateway.save(usersAddressMapper.toDomain(users, address));

    return mapper.toOutput(usersAddress.getAddress());
  }
}
