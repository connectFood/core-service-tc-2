package com.connectfood.core.application.restaurantitems.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;

import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

@Getter
public class RestaurantItemsInput {

  private final String name;
  private final String description;
  private final BigDecimal value;
  private final RestaurantItemServiceType requestType;
  private final UUID restaurantUuid;

  public RestaurantItemsInput(final String name, final String description, final BigDecimal value,
      final RestaurantItemServiceType requestType, final UUID restaurantUuid) {
    this.name = name;
    this.description = description;
    this.value = value;
    this.requestType = requestType;
    this.restaurantUuid = restaurantUuid;
  }
}
