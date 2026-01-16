package com.connectfood.core.application.restaurantitem.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.application.restaurant.dto.RestaurantOutput;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import lombok.Getter;

@Getter
public class RestaurantItemOutput {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final BigDecimal value;
  private final RestaurantItemServiceType requestType;
  private final RestaurantOutput restaurant;
  private final List<RestaurantItemImageOutput> images;

  public RestaurantItemOutput(final UUID uuid, final String name, final String description,
                              final BigDecimal value, final RestaurantItemServiceType requestType, final RestaurantOutput restaurant,
                              final List<RestaurantItemImageOutput> images) {
    this.uuid = uuid;
    this.name = name;
    this.description = description;
    this.value = value;
    this.requestType = requestType;
    this.restaurant = restaurant;
    this.images = images;
  }

  public RestaurantItemOutput(final UUID uuid, final String name, final String description,
                              final BigDecimal value, final RestaurantItemServiceType requestType, final RestaurantOutput restaurant) {
    this(uuid, name, description, value, requestType, restaurant, null);
  }

  public RestaurantItemOutput(final UUID uuid, final String name, final String description,
                              final BigDecimal value, final RestaurantItemServiceType requestType) {
    this(uuid, name, description, value, requestType, null, null);
  }
}
