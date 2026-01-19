package com.connectfood.core.domain.model;

import lombok.Getter;

import com.connectfood.core.domain.exception.BadRequestException;

import java.util.UUID;

@Getter
public class RestaurantAddress {

  private final UUID uuid;
  private final Restaurant restaurant;
  private final Address address;

  public RestaurantAddress(final UUID uuid, final Restaurant restaurant, final Address address) {
    if(restaurant == null) {
      throw new BadRequestException("Restaurant is required");
    }

    if(address == null) {
      throw new BadRequestException("Address is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.restaurant = restaurant;
    this.address = address;
  }

  public RestaurantAddress(final Restaurant restaurant, final Address address) {
    this(null, restaurant, address);
  }

}
