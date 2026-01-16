package com.connectfood.core.application.restaurant.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class UserRestaurantInput {

  private final UUID usersUuid;
  private final UUID restaurantUuid;

  public UserRestaurantInput(final UUID usersUuid, final UUID restaurantUuid) {
    this.usersUuid = usersUuid;
    this.restaurantUuid = restaurantUuid;
  }
}
