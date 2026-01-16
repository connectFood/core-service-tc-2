package com.connectfood.core.domain.model;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class Restaurant {

  private final UUID uuid;
  private final String name;
  private final RestaurantType restaurantType;
  private final List<RestaurantOpeningHour> openingHours;
  private final Address address;
  private final User user;

  public Restaurant(final UUID uuid, final String name, final RestaurantType restaurantType,
                    final List<RestaurantOpeningHour> openingHours, final Address address, final User user) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name cannot be null or blank");
    }

    if (restaurantType == null) {
      throw new BadRequestException("Restaurant type is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.restaurantType = restaurantType;
    this.openingHours = openingHours;
    this.address = address;
    this.user = user;
  }

  public Restaurant(final UUID uuid, final String name, final RestaurantType restaurantType) {
    this(uuid, name, restaurantType, null, null, null);
  }

  public Restaurant(final String name, final RestaurantType restaurantType) {
    this(null, name, restaurantType, null, null, null);
  }
}
