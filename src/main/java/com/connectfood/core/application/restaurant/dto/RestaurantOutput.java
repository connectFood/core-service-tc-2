package com.connectfood.core.application.restaurant.dto;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.restauranttype.dto.RestaurantTypeOutput;
import com.connectfood.core.application.user.dto.UserOutput;

import lombok.Getter;

@Getter
public class RestaurantOutput {

  private final UUID uuid;
  private final String name;
  private final RestaurantTypeOutput restaurantsType;
  private final List<RestaurantOpeningHourOutput> openingHours;
  private final AddressOutput address;
  private final UserOutput users;

  public RestaurantOutput(UUID uuid, String name, final RestaurantTypeOutput restaurantsType,
                          final List<RestaurantOpeningHourOutput> openingHours, final AddressOutput address, final UserOutput users) {
    this.uuid = uuid;
    this.name = name;
    this.restaurantsType = restaurantsType;
    this.openingHours = openingHours;
    this.address = address;
    this.users = users;
  }

  public RestaurantOutput(UUID uuid, String name, final RestaurantTypeOutput restaurantsType) {
    this(uuid, name, restaurantsType, null, null, null);
  }
}
