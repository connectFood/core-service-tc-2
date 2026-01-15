package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressRepository;
import com.connectfood.core.domain.repository.RestaurantsAddressRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateRestaurantsAddressUseCase {

  private final RestaurantsAddressRepository repository;
  private final AddressAppMapper mapper;
  private final RequestUserGuard guard;
  private final AddressRepository addressRepository;

  public UpdateRestaurantsAddressUseCase(
      final RestaurantsAddressRepository repository,
      final AddressAppMapper mapper,
      final RequestUserGuard guard,
      final AddressRepository addressRepository
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.addressRepository = addressRepository;
  }

  @Transactional
  public AddressOutput execute(final RequestUser requestUser, final UUID restaurantUuid,
      final AddressInput addressInput) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var restaurantsAddress = repository.findByRestaurantsUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurants Address Not Found"));

    final var addressUuid = restaurantsAddress.getAddress()
        .getUuid();

    final var addressUpdated = addressRepository.update(addressUuid, mapper.toDomain(addressUuid, addressInput));

    return mapper.toOutput(addressUpdated);
  }
}
