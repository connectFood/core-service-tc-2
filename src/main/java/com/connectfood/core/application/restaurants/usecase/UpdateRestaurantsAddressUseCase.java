package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantAddressGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantsAddressUseCase {

  private final RestaurantAddressGateway repository;
  private final AddressAppMapper mapper;
  private final RequestUserGuard guard;
  private final AddressGateway addressGateway;

  public UpdateRestaurantsAddressUseCase(
      final RestaurantAddressGateway repository,
      final AddressAppMapper mapper,
      final RequestUserGuard guard,
      final AddressGateway addressGateway
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.addressGateway = addressGateway;
  }

  @Transactional
  public AddressOutput execute(final RequestUser requestUser, final UUID restaurantUuid,
      final AddressInput addressInput) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var restaurantsAddress = repository.findByRestaurantsUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Address Not Found"));

    final var addressUuid = restaurantsAddress.getAddress()
        .getUuid();

    final var addressUpdated = addressGateway.update(addressUuid, mapper.toDomain(addressUuid, addressInput));

    return mapper.toOutput(addressUpdated);
  }
}
