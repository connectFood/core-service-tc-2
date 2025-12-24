package com.connectfood.core.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;
import com.connectfood.core.domain.model.enums.RestaurantItemServiceType;

import lombok.Getter;

@Getter
public class RestaurantItems {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final BigDecimal value;
  private final RestaurantItemServiceType requestType;
  private final Restaurants restaurant;
  private final List<RestaurantItemsImages> images;

  public RestaurantItems(final UUID uuid, final String name, final String description, final BigDecimal value,
      final RestaurantItemServiceType requestType, final Restaurants restaurant,
      final List<RestaurantItemsImages> images) {

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

  public RestaurantItems(final UUID uuid, final String name, final String description, final BigDecimal value,
      final RestaurantItemServiceType requestType, final Restaurants restaurant) {
    this(uuid, name, description, value, requestType, restaurant, null);
  }

  public RestaurantItems(final String name, final String description, final BigDecimal value,
      final RestaurantItemServiceType requestType, final Restaurants restaurant) {
    this(null, name, description, value, requestType, restaurant, null);
  }

  public RestaurantItems(final UUID uuid, final String name, final String description, final BigDecimal value,
      final RestaurantItemServiceType requestType) {
    this(uuid, name, description, value, requestType, null, null);
  }
}
