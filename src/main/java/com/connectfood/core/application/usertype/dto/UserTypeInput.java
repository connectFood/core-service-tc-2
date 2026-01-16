package com.connectfood.core.application.usertype.dto;

import lombok.Getter;

@Getter
public class UserTypeInput {

  private final String name;
  private final String description;

  public UserTypeInput(final String name, final String description) {
    this.name = name;
    this.description = description;
  }
}
