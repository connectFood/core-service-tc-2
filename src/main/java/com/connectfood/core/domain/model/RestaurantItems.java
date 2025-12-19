package com.connectfood.core.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class RestaurantItems {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final BigDecimal value;
  private final String requestType;
  private final Restaurants restaurant;

  public RestaurantItems(final UUID uuid, final String name, final String description, final BigDecimal value,
      final String requestType, final Restaurants restaurant) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name is required");
    }

    if (value == null) {
      throw new BadRequestException("Value is required");
    }

    if (requestType == null || requestType.isBlank()) {
      throw new BadRequestException("Request Type hash is required");
    }


    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.description = description;
    this.value = value;
    this.requestType = requestType;
    this.restaurant = restaurant;
  }

  public RestaurantItems(final String name, final String description, final BigDecimal value, final String requestType,
      final Restaurants restaurant) {
    this(null, name, description, value, requestType, restaurant);
  }
}
