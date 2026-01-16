package com.connectfood.core.application.usertype.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserTypeOutput {

  private final UUID uuid;
  private final String name;
  private final String description;

  public UserTypeOutput(final UUID uuid, final String name, final String description) {
    this.uuid = uuid;
    this.name = name;
    this.description = description;
  }
}
