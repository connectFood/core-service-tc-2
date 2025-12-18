package com.connectfood.core.domain.model;

import com.connectfood.core.domain.exception.BadRequestException;

import java.util.UUID;

public class Restaurants {

  private final UUID uuid;
  private final String name;
  private final RestaurantsType restaurantsType;

  public Restaurants(final UUID uuid, final String name, final RestaurantsType restaurantsType) {

    if(name == null || name.isBlank()) {
      throw new BadRequestException("Name cannot be null or blank");
    }

    if(restaurantsType == null) {
      throw new BadRequestException("Restaurant type is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.restaurantsType = restaurantsType;
  }

  public Restaurants(final String name, final RestaurantsType restaurantsType) {
    this(null, name, restaurantsType);
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public RestaurantsType getRestaurantsType() {
    return restaurantsType;
  }
}
