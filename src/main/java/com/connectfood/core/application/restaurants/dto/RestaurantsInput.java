package com.connectfood.core.application.restaurants.dto;

import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.address.dto.AddressInput;

import lombok.Getter;

@Getter
public class RestaurantsInput {

  private final String name;
  private final UUID restaurantsTypeUuid;
  private final List<RestaurantOpeningHoursInput> openingHours;
  private final AddressInput address;
  private final UUID usersUuid;

  public RestaurantsInput(final String name, final UUID restaurantsTypeUuid,
      final List<RestaurantOpeningHoursInput> openingHours, final AddressInput address, final UUID usersUuid) {
    this.name = name;
    this.restaurantsTypeUuid = restaurantsTypeUuid;
    this.openingHours = openingHours;
    this.address = address;
    this.usersUuid = usersUuid;
  }
}
