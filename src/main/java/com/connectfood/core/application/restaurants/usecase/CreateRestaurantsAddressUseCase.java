package com.connectfood.core.application.restaurants.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurants.mapper.RestaurantsAddressAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantsAddressGateway;
import com.connectfood.core.domain.repository.RestaurantsGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantsAddressUseCase {

  private final AddressGateway repository;
  private final AddressAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantsGateway restaurantsGateway;
  private final RestaurantsAddressGateway restaurantsAddressGateway;
  private final RestaurantsAddressAppMapper restaurantsAddressMapper;


  public CreateRestaurantsAddressUseCase(
      final AddressGateway repository,
      final AddressAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantsGateway restaurantsGateway,
      final RestaurantsAddressGateway restaurantsAddressGateway,
      final RestaurantsAddressAppMapper restaurantsAddressMapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantsGateway = restaurantsGateway;
    this.restaurantsAddressGateway = restaurantsAddressGateway;
    this.restaurantsAddressMapper = restaurantsAddressMapper;
  }

  @Transactional
  public AddressOutput execute(final RequestUser requestUser, final UUID restaurantUuid, final AddressInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var restaurants = restaurantsGateway.findByUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var address = repository.save(mapper.toDomain(input));
    final var restaurantsAddress = restaurantsAddressGateway.save(restaurantsAddressMapper.toDomain(restaurants,
        address
    ));

    return mapper.toOutput(restaurantsAddress.getAddress());
  }
}
