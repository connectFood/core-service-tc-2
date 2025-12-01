package com.connectfood.core.domain.model;

import java.util.UUID;

import com.connectfood.core.domain.exception.BadRequestException;

import lombok.Getter;

@Getter
public class UsersType {

  private final UUID uuid;
  private final String name;
  private final String description;

  public UsersType(final UUID uuid, final String name, final String description) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name is required");
    }

    this.uuid = uuid;
    this.name = name;
    this.description = description;
  }

  public UsersType(final String name, final String description) {

    if (name == null || name.isBlank()) {
      throw new BadRequestException("Name is required");
    }

    this.uuid = UUID.randomUUID();
    this.name = name;
    this.description = description;
  }
}
