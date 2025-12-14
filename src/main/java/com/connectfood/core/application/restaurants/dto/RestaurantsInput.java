package com.connectfood.core.application.restaurants.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RestaurantsInput {

  private final String name;
  private final UUID restaurantsTypeUuid;

  public RestaurantsInput(final String name, final UUID restaurantsTypeUuid) {
    this.name = name;
    this.restaurantsTypeUuid = restaurantsTypeUuid;
  }
}
