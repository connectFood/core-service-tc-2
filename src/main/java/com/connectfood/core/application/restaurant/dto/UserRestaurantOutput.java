package com.connectfood.core.application.restaurant.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class UserRestaurantOutput {

  private final UUID uuid;
  private final UUID usersUuid;
  private final UUID restaurantUuid;

  public UserRestaurantOutput(
      final UUID uuid,
      final UUID usersUuid,
      final UUID restaurantUuid
  ) {
    this.uuid = uuid;
    this.usersUuid = usersUuid;
    this.restaurantUuid = restaurantUuid;
  }
}

