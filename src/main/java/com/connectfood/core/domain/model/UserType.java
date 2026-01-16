package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class UserType {

  private final UUID uuid;
  private final String name;
  private final String description;

  public UserType(final UUID uuid, final String name, final String description) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name is required");
    }

    if (name.length() < 3 || name.length() > 255) {
      throw new BadRequestException("Name must be between 3 and 255 characters");
    }

    this.uuid = uuid == null ? UUID.randomUUID() : uuid;
    this.name = name;
    this.description = description;
  }

  public UserType(final String name, final String description) {
    this(null, name, description);
  }
}
