package com.connectfood.core.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import lombok.Getter;

@Getter
public class RestaurantItem {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final BigDecimal value;
  private final RestaurantItemServiceType requestType;
  private final Restaurant restaurant;
  private final List<RestaurantItemImage> images;

  public RestaurantItem(final UUID uuid, final String name, final String description, final BigDecimal value,
                        final RestaurantItemServiceType requestType, final Restaurant restaurant,
                        final List<RestaurantItemImage> images) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name is required");
    }

    if (value == null) {
      throw new BadRequestException("Value is required");
    }

    if (requestType == null) {
      throw new BadRequestException("Request Type hash is required");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.description = description;
    this.value = value;
    this.requestType = requestType;
    this.restaurant = restaurant;
    this.images = images;
  }

  public RestaurantItem(final UUID uuid, final String name, final String description, final BigDecimal value,
                        final RestaurantItemServiceType requestType, final Restaurant restaurant) {
    this(uuid, name, description, value, requestType, restaurant, null);
  }

  public RestaurantItem(final String name, final String description, final BigDecimal value,
                        final RestaurantItemServiceType requestType, final Restaurant restaurant) {
    this(null, name, description, value, requestType, restaurant, null);
  }

  public RestaurantItem(final UUID uuid, final String name, final String description, final BigDecimal value,
                        final RestaurantItemServiceType requestType) {
    this(uuid, name, description, value, requestType, null, null);
  }
}
