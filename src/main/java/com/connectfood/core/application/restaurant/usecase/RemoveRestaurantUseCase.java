package com.connectfood.core.application.restaurant.usecase;

import java.util.UUID;

import com.connectfood.core.application.security.RequestUser;
import com.connectfood.core.application.security.RequestUserGuard;
import com.connectfood.core.domain.exception.NotFoundException;
import com.connectfood.core.domain.model.enums.UsersType;
import com.connectfood.core.domain.repository.AddressGateway;
import com.connectfood.core.domain.repository.RestaurantAddressGateway;
import com.connectfood.core.domain.repository.RestaurantGateway;
import com.connectfood.core.domain.repository.RestaurantItemGateway;
import com.connectfood.core.domain.repository.RestaurantItemImageGateway;
import com.connectfood.core.domain.repository.RestaurantOpeningHourGateway;
import com.connectfood.core.domain.repository.UserRestaurantGateway;

import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class RemoveRestaurantUseCase {

  private final RestaurantGateway repository;
  private final RequestUserGuard guard;
  private final RestaurantItemGateway restaurantItemGateway;
  private final RestaurantItemImageGateway restaurantItemImageGateway;
  private final RestaurantOpeningHourGateway restaurantOpeningHourGateway;
  private final RestaurantAddressGateway restaurantAddressGateway;
  private final AddressGateway addressGateway;
  private final UserRestaurantGateway userRestaurantGateway;

  public RemoveRestaurantUseCase(
      final RestaurantGateway repository,
      final RequestUserGuard guard,
      final RestaurantItemGateway restaurantItemGateway,
      final RestaurantItemImageGateway restaurantItemImageGateway,
      final RestaurantOpeningHourGateway restaurantOpeningHourGateway,
      final RestaurantAddressGateway restaurantAddressGateway,
      final AddressGateway addressGateway,
      final UserRestaurantGateway userRestaurantGateway
  ) {
    this.repository = repository;
    this.guard = guard;
    this.restaurantItemGateway = restaurantItemGateway;
    this.restaurantItemImageGateway = restaurantItemImageGateway;
    this.restaurantOpeningHourGateway = restaurantOpeningHourGateway;
    this.restaurantAddressGateway = restaurantAddressGateway;
    this.addressGateway = addressGateway;
    this.userRestaurantGateway = userRestaurantGateway;
  }

  @Transactional
  public void execute(final RequestUser requestUser, final UUID uuid) {
    guard.requireRole(requestUser, UsersType.OWNER.name());

    repository.findByUuid(uuid)
        .orElseThrow(() -> new NotFoundException("Restaurants not found"));

    final var existsItems = restaurantItemGateway.existsByRestaurantUuid(uuid);

    if (existsItems) {
      final var restaurantItems = restaurantItemGateway.findAllByRestaurantUuid(uuid);

      for (final var restaurantItem : restaurantItems) {
        final var existsImages = restaurantItemImageGateway.existsByRestaurantItemUuid(uuid);

        if (existsImages) {
          restaurantItemImageGateway.deleteByRestaurantItemUuid(uuid);
        }

        restaurantItemGateway.delete(restaurantItem.getUuid());
      }
    }

    final var existsOpeningHour = restaurantOpeningHourGateway.existsByRestaurantUuid(uuid);

    if (existsOpeningHour) {
      restaurantOpeningHourGateway.deleteByRestaurantUuid(uuid);
    }

    final var existsAddress = restaurantAddressGateway.existsByRestaurantsUuid(uuid);

    if (existsAddress) {
      final var restaurantAddress = restaurantAddressGateway.findByRestaurantsUuid(uuid)
          .orElseThrow();

      restaurantAddressGateway.delete(restaurantAddress.getUuid());

      addressGateway.delete(restaurantAddress.getAddress()
          .getUuid());
    }

    final var existsUsers = userRestaurantGateway.existsByRestaurantsUuid(uuid);

    if (existsUsers) {
      userRestaurantGateway.deleteByRestaurantsUuid(uuid);
    }

    repository.delete(uuid);
  }
}
