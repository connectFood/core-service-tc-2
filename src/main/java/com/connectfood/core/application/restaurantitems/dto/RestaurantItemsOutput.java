package com.connectfood.core.application.restaurantitems.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.application.restaurants.dto.RestaurantsOutput;

import lombok.Getter;

@Getter
public class RestaurantItemsOutput {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final BigDecimal value;
  private final String requestType;
  private final RestaurantsOutput restaurant;

  public RestaurantItemsOutput(final UUID uuid, final String name, final String description,
      final BigDecimal value, final String requestType, final RestaurantsOutput restaurant) {
    this.uuid = uuid;
    this.name = name;
    this.description = description;
    this.value = value;
    this.requestType = requestType;
    this.restaurant = restaurant;
  }

  public RestaurantItemsOutput(final UUID uuid, final String name, final String description,
      final BigDecimal value, final String requestType) {
    this(uuid, name, description, value, requestType, null);
  }
}
