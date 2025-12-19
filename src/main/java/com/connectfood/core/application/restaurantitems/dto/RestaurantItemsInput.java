package com.connectfood.core.application.restaurantitems.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;

@Getter
public class RestaurantItemsInput {

  private final String name;
  private final String description;
  private final BigDecimal value;
  private final String requestType;
  private final UUID restaurantUuid;

  public RestaurantItemsInput(final String name, final String description, final BigDecimal value,
      final String requestType, final UUID restaurantUuid) {
    this.name = name;
    this.description = description;
    this.value = value;
    this.requestType = requestType;
    this.restaurantUuid = restaurantUuid;
  }
}
