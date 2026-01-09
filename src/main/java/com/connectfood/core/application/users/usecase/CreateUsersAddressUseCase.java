package com.connectfood.core.application.users.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.users.mapper.UsersAddressAppMapper;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.UsersAddressRepository;
import com.connectfood.core.domain.repository.UsersRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateUsersAddressUseCase {

  private final AddressRepository repository;
  private final AddressAppMapper mapper;
  private final UsersRepository usersRepository;
  private final UsersAddressRepository usersAddressRepository;
  private final UsersAddressAppMapper usersAddressMapper;

  public CreateUsersAddressUseCase(
      final AddressRepository repository,
      final AddressAppMapper mapper,
      final UsersRepository usersRepository,
      final UsersAddressRepository usersAddressRepository,
      final UsersAddressAppMapper usersAddressMapper) {
    this.repository = repository;
    this.mapper = mapper;
    this.usersRepository = usersRepository;
    this.usersAddressRepository = usersAddressRepository;
    this.usersAddressMapper = usersAddressMapper;
  }

  @Transactional
  public AddressOutput execute(final UUID userUuid, final AddressInput input) {
    final var users = usersRepository.findByUuid(userUuid)
        .orElseThrow(() -> new NotFoundException("User not found"));

    final var address = repository.save(mapper.toDomain(input));

    final var usersAddress = usersAddressRepository.save(usersAddressMapper.toDomain(users, address));

    return mapper.toOutput(usersAddress.getAddress());
  }
}
