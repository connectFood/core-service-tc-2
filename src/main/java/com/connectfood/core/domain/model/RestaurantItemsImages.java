package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class RestaurantItemsImages {

  private final UUID uuid;
  private final String name;
  private final String description;
  private final String path;

  public RestaurantItemsImages(final UUID uuid, final String name, final String description, final String path) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name is required");
    }

    if (path == null || path.isBlank()) {
      throw new BadRequestException("Path hash is required");
    }


    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.description = description;
    this.path = path;
  }

  public RestaurantItemsImages(final String name, final String description, final String path) {
    this(null, name, description, path);
  }
}
