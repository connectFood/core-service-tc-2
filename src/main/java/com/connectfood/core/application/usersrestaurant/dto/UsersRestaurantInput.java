package com.connectfood.core.application.usersrestaurant.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class UsersRestaurantInput {

  private final UUID usersUuid;
  private final UUID restaurantUuid;

  public UsersRestaurantInput(final UUID usersUuid, final UUID restaurantUuid) {
    this.usersUuid = usersUuid;
    this.restaurantUuid = restaurantUuid;
  }
}
