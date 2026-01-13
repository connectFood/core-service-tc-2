package com.connectfood.core.domain.model;

import lombok.Getter;

import com.connectfood.core.domain.exception.BadRequestException;

import java.util.UUID;

@Getter
public class RestaurantsAddress {

  private final UUID uuid;
  private final Restaurants restaurants;
  private final Address address;

  public RestaurantsAddress(final UUID uuid, final Restaurants restaurants, final Address address) {
    if(restaurants == null) {
      throw new BadRequestException("Restaurant is required");
    }

    if(address == null) {
      throw new BadRequestException("Address is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.restaurants = restaurants;
    this.address = address;
  }

  public RestaurantsAddress(final Restaurants restaurants, final Address address) {
    this(null, restaurants, address);
  }

}
