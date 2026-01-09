package com.connectfood.core.domain.model;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class Restaurants {

  private final UUID uuid;
  private final String name;
  private final RestaurantsType restaurantsType;
  private final List<RestaurantOpeningHours> openingHours;
  private final Address address;
  private final Users users;

  public Restaurants(final UUID uuid, final String name, final RestaurantsType restaurantsType,
      final List<RestaurantOpeningHours> openingHours, final Address address, final Users users) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name cannot be null or blank");
    }

    if (restaurantsType == null) {
      throw new BadRequestException("Restaurant type is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.restaurantsType = restaurantsType;
    this.openingHours = openingHours;
    this.address = address;
    this.users = users;
  }

  public Restaurants(final UUID uuid, final String name, final RestaurantsType restaurantsType) {
    this(uuid, name, restaurantsType, null, null, null);
  }

  public Restaurants(final String name, final RestaurantsType restaurantsType) {
    this(null, name, restaurantsType, null, null, null);
  }
}
