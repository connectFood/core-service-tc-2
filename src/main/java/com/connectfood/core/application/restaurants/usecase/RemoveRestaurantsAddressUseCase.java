package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveRestaurantsAddressUseCase {

  private final RestaurantsAddressRepository repository;
  private final AddressRepository addressRepository;
  private final RequestUserGuard guard;

  public RemoveRestaurantsAddressUseCase(
      final RestaurantsAddressRepository repository,
      final AddressRepository addressRepository,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.addressRepository = addressRepository;
    this.guard = guard;
  }

  @Transactional
  public void execute(final RequestUser requestUser, final UUID uuid) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var model = repository.findByRestaurantsUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Address Not Found"));

    repository.delete(model.getUuid());

    addressRepository.delete(model.getAddress()
        .getUuid());
  }
}
