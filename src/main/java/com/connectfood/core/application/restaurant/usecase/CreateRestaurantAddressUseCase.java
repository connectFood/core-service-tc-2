package com.connectfood.core.application.restaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;
import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.address.mapper.AddressAppMapper;
import com.connectfood.core.application.restaurant.mapper.RestaurantAddressAppMapper;
import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantAddressGateway;
import com.connectfood.core.domain.repository.RestaurantGateway;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateRestaurantAddressUseCase {

  private final AddressGateway repository;
  private final AddressAppMapper mapper;
  private final RequestUserGuard guard;
  private final RestaurantGateway restaurantGateway;
  private final RestaurantAddressGateway restaurantAddressGateway;
  private final RestaurantAddressAppMapper restaurantsAddressMapper;


  public CreateRestaurantAddressUseCase(
      final AddressGateway repository,
      final AddressAppMapper mapper,
      final RequestUserGuard guard,
      final RestaurantGateway restaurantGateway,
      final RestaurantAddressGateway restaurantAddressGateway,
      final RestaurantAddressAppMapper restaurantsAddressMapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
    this.guard = guard;
    this.restaurantGateway = restaurantGateway;
    this.restaurantAddressGateway = restaurantAddressGateway;
    this.restaurantsAddressMapper = restaurantsAddressMapper;
  }

  @Transactional
  public AddressOutput execute(final RequestUser requestUser, final UUID restaurantUuid, final AddressInput input) {
    guard.requireRole(requestUser, UsersType.OWNER.name());
    final var restaurants = restaurantGateway.findByUuid(restaurantUuid)
        .orElseThrow(() -> new NotFoundException("Restaurant not found"));

    final var address = repository.save(mapper.toDomain(input));
    final var restaurantsAddress = restaurantAddressGateway.save(restaurantsAddressMapper.toDomain(restaurants,
        address
    ));

    return mapper.toOutput(restaurantsAddress.getAddress());
  }
}
