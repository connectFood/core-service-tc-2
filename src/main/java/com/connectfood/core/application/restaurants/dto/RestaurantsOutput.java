package com.connectfood.core.application.restaurants.dto;

import com.connectfood.core.application.restaurantstype.dto.RestaurantsTypeOutput;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RestaurantsOutput {

  private final UUID uuid;
  private final String name;
  private final RestaurantsTypeOutput restaurantsType;

  public RestaurantsOutput(UUID uuid, String name, final RestaurantsTypeOutput restaurantsType) {
    this.uuid = uuid;
    this.name = name;
    this.restaurantsType = restaurantsType;
  }
}
