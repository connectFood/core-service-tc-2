package com.connectfood.core.application.usertype.dto;

import lombok.Getter;

@Getter
public class UsersTypeInput {

  private final String name;
  private final String description;

  public UsersTypeInput(final String name, final String description) {
    this.name = name;
    this.description = description;
  }
}
