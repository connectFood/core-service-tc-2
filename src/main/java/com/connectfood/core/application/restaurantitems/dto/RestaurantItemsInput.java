package com.connectfood.core.application.restaurantitems.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import lombok.Getter;

@Getter
public class RestaurantItemsInput {

  private final String name;
  private final String description;
  private final BigDecimal value;
  private final RestaurantItemServiceType requestType;
  private final UUID restaurantUuid;
  private final List<RestaurantItemsImagesInput> images;

  public RestaurantItemsInput(final String name, final String description, final BigDecimal value,
      final RestaurantItemServiceType requestType, final UUID restaurantUuid,
      final List<RestaurantItemsImagesInput> images) {
    this.name = name;
    this.description = description;
    this.value = value;
    this.requestType = requestType;
    this.restaurantUuid = restaurantUuid;
    this.images = images;
  }
}
