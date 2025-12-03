package com.connectfood.core.application.users.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class UsersInput {

  private final String fullName;
  private final String email;
  private final String password;
  private final UUID usersTypeUuid;

  public UsersInput(final String fullName, final String email, final String password, final UUID usersTypeUuid) {
    this.fullName = fullName;
    this.email = email;
    this.password = password;
    this.usersTypeUuid = usersTypeUuid;
  }
}
