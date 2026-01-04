package com.connectfood.core.domain.model;

import lombok.Getter;

import com.connectfood.core.domain.exception.BadRequestException;

import java.util.UUID;

@Getter
public class RestaurantsType {

  private final UUID uuid;
  private final String name;
  private final String description;


  public RestaurantsType(final UUID uuid, final String name, final String description) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name cannot be null or blank");
    }

    if (name.length() < 3 || name.length() > 255) {
      throw new BadRequestException("Name length must be between 3 and 255 characters");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.description = description;
  }

  public RestaurantsType(final String name, final String description) {
    this(null, name, description);
  }

}
