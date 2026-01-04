package com.connectfood.core.application.restaurantitems.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class RestaurantItemsImagesOutput {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final String path;

  public RestaurantItemsImagesOutput(final UUID uuid, final String name, final String description, final String path) {
    this.uuid = uuid;
    this.name = name;
    this.description = description;
    this.path = path;
  }
}
