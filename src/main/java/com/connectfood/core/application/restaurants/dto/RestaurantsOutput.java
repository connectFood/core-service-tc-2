package com.connectfood.core.application.restaurants.dto;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressOutput;
import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;
import com.connectfood.core.application.users.dto.UsersOutput;

import lombok.Getter;

@Getter
public class RestaurantsOutput {

  private final UUID uuid;
  private final String name;
  private final RestaurantsTypeOutput restaurantsType;
  private final List<RestaurantOpeningHoursOutput> openingHours;
  private final AddressOutput address;
  private final UsersOutput users;

  public RestaurantsOutput(UUID uuid, String name, final RestaurantsTypeOutput restaurantsType,
      final List<RestaurantOpeningHoursOutput> openingHours, final AddressOutput address, final UsersOutput users) {
    this.uuid = uuid;
    this.name = name;
    this.restaurantsType = restaurantsType;
    this.openingHours = openingHours;
    this.address = address;
    this.users = users;
  }

  public RestaurantsOutput(UUID uuid, String name, final RestaurantsTypeOutput restaurantsType) {
    this(uuid, name, restaurantsType, null, null, null);
  }
}
