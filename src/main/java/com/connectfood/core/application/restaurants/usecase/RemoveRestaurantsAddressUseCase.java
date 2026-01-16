package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantAddressGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RemoveRestaurantsAddressUseCase {

  private final RestaurantAddressGateway repository;
  private final AddressGateway addressGateway;
  private final RequestUserGuard guard;

  public RemoveRestaurantsAddressUseCase(
      final RestaurantAddressGateway repository,
      final AddressGateway addressGateway,
      final RequestUserGuard guard
  ) {
    this.repository = repository;
    this.addressGateway = addressGateway;
    this.guard = guard;
  }

  @Transactional
  public void execute(final RequestUser requestUser, final UUID uuid) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var model = repository.findByRestaurantsUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Address Not Found"));

    repository.delete(model.getUuid());

    addressGateway.delete(model.getAddress()
        .getUuid());
  }
}
