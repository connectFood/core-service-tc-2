package com.connectfood.core.application.restaurantitems.dto;

import lombok.Getter;

@Getter
public class RestaurantItemsImagesInput {

  private final String name;
  private final String description;
  private final String path;

  public RestaurantItemsImagesInput(final String name, final String description, final String path) {
    this.name = name;
    this.description = description;
    this.path = path;
  }
}
