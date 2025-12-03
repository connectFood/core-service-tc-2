package com.connectfood.core.application.usertype.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UsersTypeOutput {

  private final UUID uuid;
  private final String name;
  private final String description;

  public UsersTypeOutput(final UUID uuid, final String name, final String description) {
    this.uuid = uuid;
    this.name = name;
    this.description = description;
  }
}
